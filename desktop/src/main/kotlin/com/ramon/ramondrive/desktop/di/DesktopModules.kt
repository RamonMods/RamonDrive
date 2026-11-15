package com.ramon.ramondrive.desktop.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ramon.ramondrive.core.crypto.CredentialCipher
import com.ramon.ramondrive.core.crypto.FileWrappedKeyRepository
import com.ramon.ramondrive.core.crypto.KeyBackupCodec
import com.ramon.ramondrive.core.crypto.PassphraseKdf
import com.ramon.ramondrive.core.crypto.SecureFileDeleter
import com.ramon.ramondrive.core.crypto.StreamCrypto
import com.ramon.ramondrive.core.crypto.TdlibDatabaseKeyProviderImpl
import com.ramon.ramondrive.core.crypto.WrappedKeyRepository
import com.ramon.ramondrive.core.dispatchers.DefaultDispatcherProvider
import com.ramon.ramondrive.core.dispatchers.DispatcherProvider
import com.ramon.ramondrive.core.files.AppStoragePaths
import com.ramon.ramondrive.core.files.DownloadWriter
import com.ramon.ramondrive.core.files.FileImporter
import com.ramon.ramondrive.core.files.LocalCopyDeleter
import com.ramon.ramondrive.core.files.PendingShare
import com.ramon.ramondrive.core.files.sharedFilesModule
import com.ramon.ramondrive.core.logging.debugLogModule
import com.ramon.ramondrive.core.media.MediaMetadataExtractor
import com.ramon.ramondrive.core.media.ThumbnailMemoryCache
import com.ramon.ramondrive.core.media.ThumbnailStore
import com.ramon.ramondrive.core.network.NetworkMonitor
import com.ramon.ramondrive.core.permissions.PermissionChecker
import com.ramon.ramondrive.core.power.PowerMonitor
import com.ramon.ramondrive.core.proxy.ProxyProbe
import com.ramon.ramondrive.core.publish.PublishOutboxDrainer
import com.ramon.ramondrive.core.publish.PublishScheduler
import com.ramon.ramondrive.core.security.AppLockManager
import com.ramon.ramondrive.core.telegram.DesktopTelegramClient
import com.ramon.ramondrive.core.telegram.TdlibDatabaseKeyProvider
import com.ramon.ramondrive.core.telegram.TelegramClient
import com.ramon.ramondrive.core.telegram.TelegramPacer
import com.ramon.ramondrive.core.transfer.BackupResumeScheduler
import com.ramon.ramondrive.core.transfer.BackupSessionTracker
import com.ramon.ramondrive.core.transfer.CountingBackupSessionTracker
import com.ramon.ramondrive.core.transfer.MaintenanceScheduler
import com.ramon.ramondrive.core.transfer.TransferErrorMessages
import com.ramon.ramondrive.core.transfer.TransferScheduler
import com.ramon.ramondrive.core.transfer.transferEngineModule
import com.ramon.ramondrive.core.update.UpdateChecker
import com.ramon.ramondrive.data.local.database.MIGRATION_10_11
import com.ramon.ramondrive.data.local.database.MIGRATION_11_12
import com.ramon.ramondrive.data.local.database.MIGRATION_12_13
import com.ramon.ramondrive.data.local.database.MIGRATION_13_14
import com.ramon.ramondrive.data.local.database.MIGRATION_14_15
import com.ramon.ramondrive.data.local.database.MIGRATION_15_16
import com.ramon.ramondrive.data.local.database.MIGRATION_16_17
import com.ramon.ramondrive.data.local.database.MIGRATION_1_2
import com.ramon.ramondrive.data.local.database.MIGRATION_2_3
import com.ramon.ramondrive.data.local.database.MIGRATION_3_4
import com.ramon.ramondrive.data.local.database.MIGRATION_4_5
import com.ramon.ramondrive.data.local.database.MIGRATION_5_6
import com.ramon.ramondrive.data.local.database.MIGRATION_6_7
import com.ramon.ramondrive.data.local.database.MIGRATION_7_8
import com.ramon.ramondrive.data.local.database.MIGRATION_8_9
import com.ramon.ramondrive.data.local.database.MIGRATION_9_10
import com.ramon.ramondrive.data.local.database.RamonDriveDatabase
import com.ramon.ramondrive.data.local.database.daosModule
import com.ramon.ramondrive.data.repository.LocalDataWiper
import com.ramon.ramondrive.data.repository.repositoryModule
import com.ramon.ramondrive.desktop.BuildInfo
import com.ramon.ramondrive.desktop.DesktopPlatformCapabilities
import com.ramon.ramondrive.desktop.crypto.DpapiCredentialCipher
import com.ramon.ramondrive.desktop.crypto.LocalKeyCredentialCipher
import com.ramon.ramondrive.desktop.data.DesktopLocalDataWiper
import com.ramon.ramondrive.desktop.files.DesktopDownloadWriter
import com.ramon.ramondrive.desktop.files.DesktopFileImporter
import com.ramon.ramondrive.desktop.files.DesktopLocalCopyDeleter
import com.ramon.ramondrive.desktop.files.DesktopPendingShare
import com.ramon.ramondrive.desktop.files.DesktopStandardFolderPaths
import com.ramon.ramondrive.desktop.files.DesktopStoragePaths
import com.ramon.ramondrive.desktop.media.DesktopMediaMetadataExtractor
import com.ramon.ramondrive.desktop.media.DesktopThumbnailStore
import com.ramon.ramondrive.desktop.media.ExternalMediaPlayer
import com.ramon.ramondrive.desktop.media.MediaStreamServer
import com.ramon.ramondrive.desktop.media.NoopThumbnailMemoryCache
import com.ramon.ramondrive.desktop.network.DesktopNetworkMonitor
import com.ramon.ramondrive.desktop.permissions.DesktopPermissionChecker
import com.ramon.ramondrive.desktop.power.DesktopPowerMonitor
import com.ramon.ramondrive.desktop.publish.DesktopPublishScheduler
import com.ramon.ramondrive.desktop.transfer.DesktopBackupResumeScheduler
import com.ramon.ramondrive.desktop.transfer.DesktopMaintenanceScheduler
import com.ramon.ramondrive.desktop.transfer.DesktopTransferErrorMessages
import com.ramon.ramondrive.desktop.transfer.DesktopTransferScheduler
import com.ramon.ramondrive.domain.usecase.useCaseModule
import com.ramon.ramondrive.presentation.di.sharedUiModule
import com.ramon.ramondrive.presentation.platform.PlatformCapabilities
import com.ramon.ramondrive.presentation.platform.StandardFolderPaths
import com.ramon.ramondrive.presentation.platform.NoopUpdateInstaller
import com.ramon.ramondrive.presentation.platform.UpdateInstaller
import java.io.File
import kotlinx.coroutines.Dispatchers
import okio.Path.Companion.toOkioPath
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val desktopModule = module {
    includes(
        daosModule,
        repositoryModule,
        useCaseModule,
        sharedFilesModule,
        debugLogModule,
        transferEngineModule,
        sharedUiModule
    )

    singleOf(::DesktopStoragePaths) bind AppStoragePaths::class
    single<CredentialCipher> {
        val os = System.getProperty("os.name").orEmpty().lowercase()
        if (os.contains("win")) DpapiCredentialCipher() else LocalKeyCredentialCipher(get())
    }
    singleOf(::FileWrappedKeyRepository) bind WrappedKeyRepository::class
    singleOf(::TdlibDatabaseKeyProviderImpl) bind TdlibDatabaseKeyProvider::class
    singleOf(::DefaultDispatcherProvider) bind DispatcherProvider::class
    singleOf(::StreamCrypto)
    singleOf(::SecureFileDeleter)
    singleOf(::PassphraseKdf)
    singleOf(::KeyBackupCodec)
    singleOf(::TelegramPacer)
    singleOf(::ProxyProbe)
    singleOf(::AppLockManager)
    single { UpdateChecker(BuildInfo.VERSION) }
    single<UpdateInstaller> { NoopUpdateInstaller }
    singleOf(::DesktopPermissionChecker) bind PermissionChecker::class
    singleOf(::DesktopStandardFolderPaths) bind StandardFolderPaths::class
    singleOf(::DesktopPlatformCapabilities) bind PlatformCapabilities::class
    singleOf(::PublishOutboxDrainer)
    singleOf(::DesktopNetworkMonitor) bind NetworkMonitor::class
    singleOf(::DesktopPowerMonitor) bind PowerMonitor::class
    singleOf(::DesktopTransferErrorMessages) bind TransferErrorMessages::class
    singleOf(::DesktopDownloadWriter) bind DownloadWriter::class
    singleOf(::MediaStreamServer)
    singleOf(::ExternalMediaPlayer)
    singleOf(::DesktopFileImporter) bind FileImporter::class
    singleOf(::DesktopPendingShare) bind PendingShare::class
    singleOf(::DesktopLocalCopyDeleter) bind LocalCopyDeleter::class
    singleOf(::DesktopMediaMetadataExtractor) bind MediaMetadataExtractor::class
    singleOf(::DesktopThumbnailStore) bind ThumbnailStore::class
    singleOf(::NoopThumbnailMemoryCache) bind ThumbnailMemoryCache::class
    singleOf(::CountingBackupSessionTracker) bind BackupSessionTracker::class
    singleOf(::DesktopTransferScheduler) bind TransferScheduler::class
    singleOf(::DesktopBackupResumeScheduler) bind BackupResumeScheduler::class
    singleOf(::DesktopMaintenanceScheduler) bind MaintenanceScheduler::class
    singleOf(::DesktopLocalDataWiper) bind LocalDataWiper::class
    singleOf(::DesktopPublishScheduler) bind PublishScheduler::class
    single<TelegramClient> {
        DesktopTelegramClient(get(), get(), get(), BuildInfo.VERSION)
    }
    single<DataStore<Preferences>> {
        val storagePaths = get<AppStoragePaths>()
        PreferenceDataStoreFactory.createWithPath {
            File(File(storagePaths.filesDir, "datastore"), "settings.preferences_pb")
                .apply { parentFile?.mkdirs() }
                .toOkioPath()
        }
    }
    single<RamonDriveDatabase> {
        val storagePaths = get<AppStoragePaths>()
        Room.databaseBuilder<RamonDriveDatabase>(
            name = File(storagePaths.filesDir, "ramondrive.db").absolutePath
        )
            .addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3,
                MIGRATION_3_4,
                MIGRATION_4_5,
                MIGRATION_5_6,
                MIGRATION_6_7,
                MIGRATION_7_8,
                MIGRATION_8_9,
                MIGRATION_9_10,
                MIGRATION_10_11,
                MIGRATION_11_12,
                MIGRATION_12_13,
                MIGRATION_13_14,
                MIGRATION_14_15,
                MIGRATION_15_16,
                MIGRATION_16_17
            )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}
