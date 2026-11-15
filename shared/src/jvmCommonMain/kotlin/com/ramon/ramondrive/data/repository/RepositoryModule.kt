package com.ramon.ramondrive.data.repository

import com.ramon.ramondrive.core.telegram.TelegramSession
import com.ramon.ramondrive.data.remote.telegram.ManifestCodec
import com.ramon.ramondrive.domain.repository.BackupRepository
import com.ramon.ramondrive.domain.repository.CacheRepository
import com.ramon.ramondrive.domain.repository.ChannelRepository
import com.ramon.ramondrive.domain.repository.ExclusionRepository
import com.ramon.ramondrive.domain.repository.FileRepository
import com.ramon.ramondrive.domain.repository.KeyBackupRepository
import com.ramon.ramondrive.domain.repository.ProxyRepository
import com.ramon.ramondrive.domain.repository.SettingsRepository
import com.ramon.ramondrive.domain.repository.SyncRepository
import com.ramon.ramondrive.domain.repository.TelegramAuthRepository
import com.ramon.ramondrive.domain.repository.TransferRepository
import com.ramon.ramondrive.domain.repository.TrashRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::ManifestCodec)
    singleOf(::ActiveChannel)
    singleOf(::ChannelOwnership)
    singleOf(::FolderOwnershipRepair)
    singleOf(::FileManifestPublisher)
    singleOf(::FolderPathResolver)
    singleOf(::FolderMerger)
    singleOf(::IndexSnapshot)
    singleOf(::FolderStateSynchronizer)
    singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class
    singleOf(::FileRepositoryImpl) bind FileRepository::class
    singleOf(::TrashRepositoryImpl) bind TrashRepository::class
    singleOf(::TransferRepositoryImpl) bind TransferRepository::class
    singleOf(::BackupRepositoryImpl) bind BackupRepository::class
    singleOf(::ExclusionRepositoryImpl) bind ExclusionRepository::class
    singleOf(::TelegramAuthRepositoryImpl) bind TelegramAuthRepository::class
    singleOf(::TelegramSession)
    singleOf(::ProxyRepositoryImpl) bind ProxyRepository::class
    singleOf(::SyncRepositoryImpl) bind SyncRepository::class
    singleOf(::ChannelRepositoryImpl) bind ChannelRepository::class
    singleOf(::CacheRepositoryImpl) bind CacheRepository::class
    singleOf(::KeyBackupRepositoryImpl) bind KeyBackupRepository::class
}
