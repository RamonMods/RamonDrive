package com.ramon.ramondrive.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.ramon.ramondrive.data.local.dao.BackupDao
import com.ramon.ramondrive.data.local.dao.CacheDao
import com.ramon.ramondrive.data.local.dao.ExclusionDao
import com.ramon.ramondrive.data.local.dao.FileDao
import com.ramon.ramondrive.data.local.dao.FilePartDao
import com.ramon.ramondrive.data.local.dao.ProxyDao
import com.ramon.ramondrive.data.local.dao.FolderDao
import com.ramon.ramondrive.data.local.dao.FolderTombstoneDao
import com.ramon.ramondrive.data.local.dao.PendingDeleteDao
import com.ramon.ramondrive.data.local.dao.StorageChannelDao
import com.ramon.ramondrive.data.local.dao.ThumbnailDao
import com.ramon.ramondrive.data.local.dao.TransferDao
import com.ramon.ramondrive.data.local.entity.BackupRecordEntity
import com.ramon.ramondrive.data.local.entity.BackupSessionEntity
import com.ramon.ramondrive.data.local.entity.CacheEntryEntity
import com.ramon.ramondrive.data.local.entity.ExclusionEntity
import com.ramon.ramondrive.data.local.entity.FileEntity
import com.ramon.ramondrive.data.local.entity.FilePartEntity
import com.ramon.ramondrive.data.local.entity.ProxyEntity
import com.ramon.ramondrive.data.local.entity.FolderEntity
import com.ramon.ramondrive.data.local.entity.FolderTombstoneEntity
import com.ramon.ramondrive.data.local.entity.PendingDeleteEntity
import com.ramon.ramondrive.data.local.entity.StorageChannelEntity
import com.ramon.ramondrive.data.local.entity.ThumbnailEntity
import com.ramon.ramondrive.data.local.entity.TransferEntity

@Database(
    entities = [
        FileEntity::class,
        FolderEntity::class,
        TransferEntity::class,
        BackupSessionEntity::class,
        BackupRecordEntity::class,
        ExclusionEntity::class,
        ThumbnailEntity::class,
        CacheEntryEntity::class,
        StorageChannelEntity::class,
        PendingDeleteEntity::class,
        FilePartEntity::class,
        ProxyEntity::class,
        FolderTombstoneEntity::class
    ],
    version = 17,
    exportSchema = true
)
@ConstructedBy(RamonDriveDatabaseConstructor::class)
abstract class RamonDriveDatabase : RoomDatabase() {
    abstract fun fileDao(): FileDao
    abstract fun folderDao(): FolderDao
    abstract fun transferDao(): TransferDao
    abstract fun backupDao(): BackupDao
    abstract fun exclusionDao(): ExclusionDao
    abstract fun thumbnailDao(): ThumbnailDao
    abstract fun cacheDao(): CacheDao
    abstract fun storageChannelDao(): StorageChannelDao
    abstract fun pendingDeleteDao(): PendingDeleteDao
    abstract fun filePartDao(): FilePartDao
    abstract fun proxyDao(): ProxyDao
    abstract fun folderTombstoneDao(): FolderTombstoneDao

    companion object {
        const val NAME = "ramondrive.db"
    }
}
