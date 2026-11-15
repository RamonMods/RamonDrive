package com.ramon.ramondrive.core.publish

import com.ramon.ramondrive.core.common.AppError
import com.ramon.ramondrive.core.common.AppResult
import com.ramon.ramondrive.core.common.SafeLog
import com.ramon.ramondrive.core.telegram.TelegramClient
import com.ramon.ramondrive.core.telegram.TelegramException
import com.ramon.ramondrive.data.local.dao.FileDao
import com.ramon.ramondrive.data.local.dao.FolderDao
import com.ramon.ramondrive.data.local.dao.FolderTombstoneDao
import com.ramon.ramondrive.data.local.dao.PendingDeleteDao
import com.ramon.ramondrive.data.local.database.RamonDriveDatabase
import com.ramon.ramondrive.data.local.database.inImmediateTransaction
import com.ramon.ramondrive.data.local.entity.FileEntity
import com.ramon.ramondrive.data.repository.FileBase
import com.ramon.ramondrive.data.repository.FileManifestPublisher
import com.ramon.ramondrive.data.repository.FolderStateSynchronizer
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay

/**
 * Rows keep their flag until the caption is accepted, so a failed edit is retried rather than
 * reverted by the next sync.
 */
class PublishOutboxDrainer(
    private val fileDao: FileDao,
    private val folderDao: FolderDao,
    private val pendingDeleteDao: PendingDeleteDao,
    private val telegramClient: TelegramClient,
    private val manifestPublisher: FileManifestPublisher,
    private val folderStateSynchronizer: FolderStateSynchronizer,
    private val tombstoneDao: FolderTombstoneDao,
    private val database: RamonDriveDatabase
) {

    suspend fun drain(
        isStopped: () -> Boolean,
        onProgress: suspend (published: Int, total: Int) -> Unit = { _, _ -> },
        onWaiting: suspend (secondsLeft: Int) -> Unit = {}
    ): Boolean {
        if (!replayDeletes(isStopped)) return false

        var published = 0
        while (true) {
            if (isStopped()) return false
            val batch = fileDao.pendingPublish(BATCH_SIZE)
            if (batch.isEmpty()) break
            onProgress(published, published + fileDao.pendingPublishCount())

            var index = 0
            while (index < batch.size) {
                val entity = batch[index++]
                if (isStopped()) return false
                when (val result = manifestPublisher.publish(entity)) {
                    is AppResult.Success -> {
                        clearIfUnchanged(entity, accepted = true)
                        onProgress(++published, published + fileDao.pendingPublishCount())
                    }
                    is AppResult.Failure -> {
                        val error = result.error
                        if (error is AppError.RateLimited) {
                            if (error.retryAfterSeconds > INLINE_WAIT_LIMIT) return false
                            if (!waitOut(error.retryAfterSeconds + 1, isStopped, onWaiting)) {
                                return false
                            }
                            index--
                            continue
                        }
                        if (!isPermanent(error)) return false
                        SafeLog.w(TAG, "Dropping unpublishable manifest: $error")
                        clearIfUnchanged(entity, accepted = false)
                        onProgress(++published, published + fileDao.pendingPublishCount())
                    }
                }
            }
        }

        repeat(MAX_FOLDER_PUSHES) {
            val chatId = folderStateSynchronizer.chatId()
            if (!folderStatePending(chatId)) return true
            val revision = folderStateSynchronizer.pendingRevision()
            val before = folderSnapshot(chatId)
            val pushed = runCatching { folderStateSynchronizer.push() }
                .onFailure { SafeLog.w(TAG, "Folder state push failed", it) }
                .isSuccess
            if (!pushed) return false
            var rowsCleared = false
            database.inImmediateTransaction {
                if (folderSnapshot(chatId) == before) {
                    folderDao.clearPendingPublishIn(chatId)
                    rowsCleared = true
                }
            }
            if (rowsCleared) folderStateSynchronizer.clearPendingPublish(revision)
        }
        return !folderStatePending(folderStateSynchronizer.chatId())
    }

    private suspend fun folderStatePending(chatId: Long): Boolean =
        folderDao.pendingPublishCountIn(chatId) > 0 || folderStateSynchronizer.hasPendingPublish()

    private suspend fun waitOut(
        seconds: Int,
        isStopped: () -> Boolean,
        onWaiting: suspend (Int) -> Unit
    ): Boolean {
        var left = seconds
        while (left > 0) {
            if (isStopped()) return false
            onWaiting(left)
            val step = minOf(left, WAIT_TICK_SECONDS)
            delay((step * 1000L).milliseconds)
            left -= step
        }
        return true
    }

    private suspend fun clearIfUnchanged(published: FileEntity, accepted: Boolean) {
        database.inImmediateTransaction {
            val current = fileDao.byId(published.id) ?: return@inImmediateTransaction
            if (accepted && published.messageId != null) {
                fileDao.setRemoteBase(published.id, FileBase.of(published).toText())
            }
            val unchanged = current.copy(
                pendingPublish = published.pendingPublish,
                remoteBase = published.remoteBase
            ) == published
            if (unchanged) {
                fileDao.clearPendingPublish(published.id)
            }
        }
    }

    private suspend fun folderSnapshot(chatId: Long) = Pair(
        folderDao.allFolders(chatId).map { it.copy(pendingPublish = false) }.sortedBy { it.id },
        tombstoneDao.inChat(chatId).sortedBy { it.id }
    )

    /**
     * Deleting a message that is already gone is accepted, so replaying after a crash costs
     * nothing.
     */
    private suspend fun replayDeletes(isStopped: () -> Boolean): Boolean {
        while (true) {
            if (isStopped()) return false
            val batch = pendingDeleteDao.oldest(BATCH_SIZE)
            if (batch.isEmpty()) return true

            for ((chatId, pending) in batch.groupBy { it.chatId }) {
                val messageIds = pending.map { it.messageId }
                try {
                    telegramClient.deleteMessages(chatId, messageIds)
                } catch (e: TelegramException) {
                    if (e.code !in PERMANENT_CODES) {
                        SafeLog.w(TAG, "Replaying ${messageIds.size} deletes failed", e)
                        return false
                    }
                    SafeLog.w(TAG, "Dropping unrepeatable delete: ${e.message}")
                }
                fileDao.deleteByIds(pending.map { it.fileId })
                pendingDeleteDao.clear(chatId, messageIds)
            }
        }
    }

    /** A rejected edit never succeeds on retry, unlike a rate limit or an outage. */
    private fun isPermanent(error: AppError): Boolean =
        error is AppError.TelegramError && error.code in PERMANENT_CODES

    private companion object {
        const val TAG = "PublishOutbox"
        const val BATCH_SIZE = 100
        const val INLINE_WAIT_LIMIT = 900
        const val WAIT_TICK_SECONDS = 15
        const val MAX_FOLDER_PUSHES = 3
        val PERMANENT_CODES = 400..499
    }
}
