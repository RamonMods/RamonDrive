package com.ramon.ramondrive.data.repository

import com.ramon.ramondrive.core.common.AppResult
import com.ramon.ramondrive.core.telegram.TelegramClient
import com.ramon.ramondrive.core.telegram.TelegramException
import com.ramon.ramondrive.data.local.entity.StorageChannelEntity
import com.ramon.ramondrive.domain.repository.ExclusionRepository
import com.ramon.ramondrive.domain.repository.SyncRepository
import com.ramon.ramondrive.testing.unused
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GoneDriveRemovalTest {

    private val harness = SyncHarness()
    private val channels = harness.database.storageChannelDao()
    private var exists: Boolean? = false

    @After
    fun tearDown() = harness.close()

    @Test
    fun `removing a drive whose channel was deleted in Telegram still removes it`() = runBlocking {
        channels.upsert(StorageChannelEntity(chatId = 1, title = "RamonDrive", addedAt = 1, lastOpenedAt = 1))
        channels.upsert(StorageChannelEntity(chatId = 2, title = "RamonDrive Teste", addedAt = 1, lastOpenedAt = 0))

        val result = repository().deleteRemotely(2)

        assertTrue(result is AppResult.Success)
        assertNull(channels.byId(2))
    }

    @Test
    fun `a drive that still exists keeps its entry when deleting fails`() = runBlocking {
        exists = true
        channels.upsert(StorageChannelEntity(chatId = 1, title = "RamonDrive", addedAt = 1, lastOpenedAt = 1))
        channels.upsert(StorageChannelEntity(chatId = 2, title = "RamonDrive Teste", addedAt = 1, lastOpenedAt = 0))

        val result = repository().deleteRemotely(2)

        assertTrue(result is AppResult.Failure)
        assertTrue(channels.byId(2) != null)
    }

    private fun repository() = ChannelRepositoryImpl(
        channelDao = channels,
        telegramClient = object : TelegramClient by unused() {
            override suspend fun deleteStorageChannel(chatId: Long) {
                throw TelegramException(400, "The chat can't be deleted")
            }

            override suspend fun chatExists(chatId: Long): Boolean? = exists
        },
        settingsRepository = harness.settings,
        syncRepository = unused<SyncRepository>(),
        channelOwnership = ChannelOwnership(
            harness.database.fileDao(),
            harness.database.folderDao(),
            harness.database.exclusionDao()
        ),
        exclusionRepository = unused<ExclusionRepository>()
    )
}
