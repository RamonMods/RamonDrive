package com.ramon.ramondrive.data.repository

import com.ramon.ramondrive.core.crypto.SecureFileDeleter
import com.ramon.ramondrive.core.files.DownloadWriter
import com.ramon.ramondrive.core.files.LocalCopyDeleter
import com.ramon.ramondrive.core.publish.PublishOutboxDrainer
import com.ramon.ramondrive.data.local.entity.FolderEntity
import com.ramon.ramondrive.data.local.entity.FolderTombstoneEntity
import com.ramon.ramondrive.data.remote.telegram.RemoteFolderEntry
import com.ramon.ramondrive.data.remote.telegram.RemoteFolderState
import com.ramon.ramondrive.data.remote.telegram.RemoteFolderTombstone
import com.ramon.ramondrive.domain.repository.TransferRepository
import com.ramon.ramondrive.testing.unused
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FolderStateLifecycleTest {

    private val harness = SyncHarness()
    private val database = harness.database
    private val folders = database.folderDao()
    private val tombstones = database.folderTombstoneDao()
    private val telegram = harness.telegram

    @After
    fun tearDown() = harness.close()

    @Test
    fun `deleting the last folder marks the state pending without a folder row`() = runBlocking {
        folders.upsert(folder("x", "Work"))

        trash().deleteFolderPermanently("x")

        assertEquals(0, folders.pendingPublishCount())
        assertEquals(1L, harness.settings.preferences.first().folderStateRevision)
        assertEquals(listOf("x"), tombstones.inChat(TEST_CHAT).map { it.id })
        assertTrue(harness.scheduler.kicks > 0)
    }

    @Test
    fun `the drainer publishes a last-folder deletion and clears the revision`() = runBlocking {
        folders.upsert(folder("x", "Work"))
        telegram.publishFolderState(
            RemoteFolderState(folders = listOf(entry("x", "Work"))),
            messageId = 900
        )
        trash().deleteFolderPermanently("x")

        assertTrue(drainer().drain(isStopped = { false }))

        assertEquals(0L, harness.settings.preferences.first().folderStateRevision)
        assertEquals(0, folders.pendingPublishCount())
        assertEquals(listOf("x"), telegram.uploadedFolderState().deleted.map { it.id })
        assertTrue(900L in telegram.deletedMessages)
    }

    @Test
    fun `pushing an empty state removes the remote document`() = runBlocking {
        telegram.publishFolderState(RemoteFolderState(), messageId = 900)

        harness.folderState.push()

        assertEquals(listOf(900L), telegram.deletedMessages)
        assertTrue(telegram.uploads.none { it.first == RemoteFolderState.FILE_NAME })
    }

    @Test
    fun `pulling an empty state leaves the remote document alone`() = runBlocking {
        telegram.publishFolderState(RemoteFolderState(), messageId = 900)

        harness.folderState.pull()

        assertTrue(telegram.deletedMessages.isEmpty())
    }

    @Test
    fun `pulling keeps the index pointer of the folder document`() = runBlocking {
        telegram.publishFolderState(
            RemoteFolderState(indexMessageId = 777),
            messageId = 900
        )

        harness.folderState.pull()

        assertEquals(777L, harness.folderState.indexMessageId ?: -1L)
        assertTrue(telegram.deletedMessages.isEmpty())
    }

    @Test
    fun `pushing an empty state removes an older copy too`() = runBlocking {
        telegram.publishFolderState(RemoteFolderState(), messageId = 900)
        telegram.addOlderFolderState(
            """{"f":[{"id":"old","n":"Gone","ct":1,"mt":1,"ca":1}]}""",
            messageId = 10,
            dateSeconds = -1_000
        )

        harness.folderState.push()

        assertTrue(900L in telegram.deletedMessages)
        assertTrue(10L in telegram.deletedMessages)
        assertTrue(telegram.uploads.none { it.first == RemoteFolderState.FILE_NAME })
    }

    @Test
    fun `an empty state with unknown fields is kept`() = runBlocking {
        telegram.publishRawFolderState("""{"future":true}""", messageId = 900)

        harness.folderState.push()

        val uploaded = telegram.uploads.last { it.first == RemoteFolderState.FILE_NAME }.second
        assertTrue(uploaded.contains("\"future\""))
    }

    @Test
    fun `pulling does not resurrect a folder deleted locally`() = runBlocking {
        folders.upsert(folder("x", "Work"))
        trash().deleteFolderPermanently("x")
        telegram.publishFolderState(
            RemoteFolderState(folders = listOf(entry("x", "Work"))),
            messageId = 900
        )

        val changed = harness.folderState.pull()

        assertEquals(0, changed)
        assertNull(folders.byId("x"))
        assertTrue(telegram.deletedMessages.isEmpty())
    }

    @Test
    fun `clearing an old revision does not hide a newer change`() = runBlocking {
        harness.settings.update { it.copy(folderStateRevision = 1L) }
        harness.settings.update { it.copy(folderStateRevision = 2L) }

        harness.folderState.clearPendingPublish(1L)

        assertEquals(2L, harness.settings.preferences.first().folderStateRevision)
    }

    @Test
    fun `repairing orphaned trash folders marks the state pending`() = runBlocking {
        folders.upsert(folder("parent", "Parent").copy(trashedAt = 5_000))
        folders.upsert(folder("child", "Child").copy(parentId = "parent"))

        trash().repairTrashTree()

        assertTrue(folders.byId("child")!!.trashedAt != null)
        assertEquals(1L, harness.settings.preferences.first().folderStateRevision)
    }

    @Test
    fun `creating a folder from an import marks the state pending`() = runBlocking {
        harness.folderPaths.resolveOrCreate("Docs/2026")

        assertTrue(folders.pendingPublishCount() > 0)
    }

    @Test
    fun `recreating a folder from sync does not mark the state pending`() = runBlocking {
        harness.folderPaths.resolveOrCreate("Docs", markPending = false)

        assertEquals(0, folders.pendingPublishCount())
    }

    @Test
    fun `an expired tombstone lets the folder document disappear`() = runBlocking {
        val old = System.currentTimeMillis() - 181L * 24 * 60 * 60 * 1000
        tombstones.upsert(listOf(FolderTombstoneEntity("x", TEST_CHAT, old)))
        telegram.publishFolderState(
            RemoteFolderState(deleted = listOf(RemoteFolderTombstone("x", old))),
            messageId = 900
        )
        harness.settings.update { it.copy(folderStateRevision = 1L) }

        assertTrue(drainer().drain(isStopped = { false }))

        assertEquals(0L, harness.settings.preferences.first().folderStateRevision)
        assertTrue(900L in telegram.deletedMessages)
        assertTrue(tombstones.inChat(TEST_CHAT).isEmpty())
        assertTrue(telegram.uploads.none { it.first == RemoteFolderState.FILE_NAME })
    }

    private fun trash() = TrashRepositoryImpl(
        storagePaths = harness.storagePaths,
        fileDao = database.fileDao(),
        folderDao = folders,
        backupDao = database.backupDao(),
        thumbnailDao = database.thumbnailDao(),
        telegramClient = telegram,
        secureFileDeleter = SecureFileDeleter(),
        localCopyDeleter = unused<LocalCopyDeleter>(),
        downloadWriter = unused<DownloadWriter>(),
        publishScheduler = harness.scheduler,
        pendingDeleteDao = database.pendingDeleteDao(),
        filePartDao = database.filePartDao(),
        activeChannel = harness.activeChannel,
        transferRepository = unused<TransferRepository>(),
        tombstoneDao = tombstones,
        settingsRepository = harness.settings
    )

    private fun drainer() = PublishOutboxDrainer(
        fileDao = database.fileDao(),
        folderDao = folders,
        pendingDeleteDao = database.pendingDeleteDao(),
        telegramClient = telegram,
        manifestPublisher = FileManifestPublisher(
            telegram,
            harness.codec,
            harness.folderPaths,
            database.filePartDao()
        ),
        folderStateSynchronizer = harness.folderState,
        tombstoneDao = tombstones,
        database = database
    )

    private fun folder(id: String, name: String) = FolderEntity(
        id = id,
        chatId = TEST_CHAT,
        parentId = null,
        name = name,
        createdAt = 1_000,
        modifiedAt = 1_000,
        changedAt = 1_000
    )

    private fun entry(id: String, name: String) = RemoteFolderEntry(
        id = id,
        parentId = null,
        name = name,
        createdAt = 1_000,
        modifiedAt = 1_000,
        changedAt = 1_000
    )
}
