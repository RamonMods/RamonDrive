package com.ramon.ramondrive.data.repository

import com.ramon.ramondrive.core.common.SafeLog
import com.ramon.ramondrive.data.local.dao.FileDao
import com.ramon.ramondrive.data.local.dao.FolderDao
import com.ramon.ramondrive.data.local.dao.FolderTombstoneDao
import com.ramon.ramondrive.data.local.database.RamonDriveDatabase
import com.ramon.ramondrive.data.local.database.inImmediateTransaction
import com.ramon.ramondrive.data.local.entity.FolderEntity
import com.ramon.ramondrive.data.local.entity.FolderTombstoneEntity

class FolderMerger(
    private val folderDao: FolderDao,
    private val fileDao: FileDao,
    private val tombstoneDao: FolderTombstoneDao,
    private val database: RamonDriveDatabase
) {

    suspend fun mergeDuplicates(chatId: Long): Int {
        val merged = database.inImmediateTransaction { mergeAll(chatId) }
        if (merged > 0) SafeLog.d(TAG, "Merged $merged duplicate folders")
        return merged
    }

    private suspend fun mergeAll(chatId: Long): Int {
        var merged = 0
        repeat(MAX_PASSES) {
            val groups = folderDao.allFolders(chatId)
                .filter { it.trashedAt == null }
                .groupBy { it.parentId to it.name.lowercase() }
                .values
                .filter { it.size > 1 }
            if (groups.isEmpty()) return merged
            val now = System.currentTimeMillis()
            for (group in groups) {
                val ordered = group.sortedWith(compareBy<FolderEntity>({ it.createdAt }, { it.id }))
                val survivor = ordered.first()
                ordered.drop(1).forEach { loser ->
                    fileDao.moveAllBetweenFolders(loser.id, survivor.id)
                    fileDao.retargetTrashedBetweenFolders(loser.id, survivor.id)
                    folderDao.reparentChildren(loser.id, survivor.id, now)
                    folderDao.retargetTrashedChildren(loser.id, survivor.id, now)
                    tombstoneDao.upsert(listOf(FolderTombstoneEntity(loser.id, chatId, now)))
                    folderDao.delete(loser.id)
                    merged++
                }
            }
            folderDao.markPendingPublish()
        }
        return merged
    }

    private companion object {
        const val TAG = "FolderMerger"
        const val MAX_PASSES = 64
    }
}
