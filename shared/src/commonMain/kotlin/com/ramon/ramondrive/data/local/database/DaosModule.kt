package com.ramon.ramondrive.data.local.database

import org.koin.dsl.module

val daosModule = module {
    single { get<RamonDriveDatabase>().storageChannelDao() }
    single { get<RamonDriveDatabase>().fileDao() }
    single { get<RamonDriveDatabase>().folderDao() }
    single { get<RamonDriveDatabase>().transferDao() }
    single { get<RamonDriveDatabase>().backupDao() }
    single { get<RamonDriveDatabase>().exclusionDao() }
    single { get<RamonDriveDatabase>().thumbnailDao() }
    single { get<RamonDriveDatabase>().cacheDao() }
    single { get<RamonDriveDatabase>().pendingDeleteDao() }
    single { get<RamonDriveDatabase>().filePartDao() }
    single { get<RamonDriveDatabase>().proxyDao() }
    single { get<RamonDriveDatabase>().folderTombstoneDao() }
}
