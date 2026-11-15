package com.ramon.ramondrive.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramon.ramondrive.core.common.AppResult
import com.ramon.ramondrive.core.network.NetworkMonitor
import com.ramon.ramondrive.core.network.NetworkStatus
import com.ramon.ramondrive.core.permissions.PermissionChecker
import com.ramon.ramondrive.core.power.PowerMonitor
import com.ramon.ramondrive.core.publish.PublishScheduler
import com.ramon.ramondrive.core.security.AppLockManager
import com.ramon.ramondrive.core.transfer.BackupGate
import com.ramon.ramondrive.data.local.dao.FileDao
import com.ramon.ramondrive.data.local.dao.FolderDao
import com.ramon.ramondrive.data.repository.ActiveChannel
import com.ramon.ramondrive.domain.model.BackupTrigger
import com.ramon.ramondrive.domain.model.DriveChannel
import com.ramon.ramondrive.domain.model.StorageSlice
import com.ramon.ramondrive.domain.model.UserPreferences
import com.ramon.ramondrive.domain.repository.BackupRepository
import com.ramon.ramondrive.domain.repository.ChannelRepository
import com.ramon.ramondrive.domain.repository.FileRepository
import com.ramon.ramondrive.domain.repository.SettingsRepository
import com.ramon.ramondrive.domain.repository.SyncRepository
import com.ramon.ramondrive.domain.repository.TelegramAuthRepository
import com.ramon.ramondrive.domain.repository.TransferRepository
import com.ramon.ramondrive.presentation.common.UiText
import com.ramon.ramondrive.presentation.common.toUiText
import com.ramon.ramondrive.resources.Res
import com.ramon.ramondrive.resources.home_backing_up_new_files
import com.ramon.ramondrive.resources.home_nothing_new_to_back_up
import com.ramon.ramondrive.resources.home_queued_files
import com.ramon.ramondrive.resources.home_switched_drive
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val fileRepository: FileRepository,
    private val transferRepository: TransferRepository,
    private val backupRepository: BackupRepository,
    private val syncRepository: SyncRepository,
    private val telegramAuthRepository: TelegramAuthRepository,
    private val networkMonitor: NetworkMonitor,
    private val permissionChecker: PermissionChecker,
    private val appLockManager: AppLockManager,
    private val settingsRepository: SettingsRepository,
    private val fileDao: FileDao,
    private val activeChannel: ActiveChannel,
    private val channelRepository: ChannelRepository,
    private val powerMonitor: PowerMonitor,
    private val folderDao: FolderDao,
    private val publishScheduler: PublishScheduler
) : ViewModel() {

    private val missingPermissions = MutableStateFlow(permissionChecker.missingCritical())
    private val _scanning = MutableStateFlow(false)
    private val _messages = MutableSharedFlow<UiText>(extraBufferCapacity = 4)

    val scanning: StateFlow<Boolean> = _scanning.asStateFlow()
    val messages: SharedFlow<UiText> = _messages.asSharedFlow()

    private val activeDrive: Flow<DriveChannel?> = activeChannel.observe()
        .flatMapLatest { chatId ->
            channelRepository.observeChannels().map { channels ->
                channels.firstOrNull { it.chatId == chatId }
            }
        }

    val driveCount: StateFlow<Int> = channelRepository.observeChannels()
        .map { it.size }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 1)

    fun cycleDrive(direction: Int) {
        viewModelScope.launch {
            val channels = channelRepository.observeChannels().first()
            if (channels.size < 2) return@launch
            val index = channels.indexOfFirst { it.isActive }
            if (index < 0) return@launch
            val target = channels[(index + direction).mod(channels.size)]
            if (channelRepository.switchTo(target.chatId) is AppResult.Success) {
                _messages.tryEmit(
                    UiText.Resource(Res.string.home_switched_drive, target.displayName)
                )
            }
        }
    }

    @OptIn(FlowPreview::class)
    private val counts: Flow<HomeCounts> = activeChannel.observe().flatMapLatest { chatId ->
        fileDao.observeHomeAggregates(chatId)
            .debounce(COUNTS_DEBOUNCE_MS.milliseconds)
            .distinctUntilChanged()
            .map { aggregates ->
                HomeCounts(
                    total = aggregates.total,
                    remoteBytes = aggregates.remoteBytes,
                    backedUp = aggregates.backedUp,
                    pending = aggregates.queued,
                    failed = aggregates.failed,
                    localOnly = aggregates.localOnly,
                    offlineBytes = aggregates.offlineBytes
                )
            }
    }

    private val countsAndStorage: Flow<Triple<HomeCounts, List<StorageSlice>, Long?>> = combine(
        counts,
        fileRepository.observeStorageByCategory(),
        backupRepository.observeLastBackupAt()
    ) { totals, slices, lastBackupAt -> Triple(totals, slices, lastBackupAt) }

    val uiState: StateFlow<HomeUiState> = combine(
        countsAndStorage,
        fileRepository.observeRecent(12),
        fileRepository.observeFavoriteFolders(),
        transferRepository.observeActiveCount(),
        combine(
            backupRepository.observeActiveSession(),
            telegramAuthRepository.connectionState,
            networkMonitor.status,
            missingPermissions,
            combine(
                settingsRepository.preferences,
                syncRepository.syncing,
                activeDrive,
                powerMonitor.charging,
                ::HomeSettings
            )
        ) { session, connection, network, missing, settings ->
            val (prefs, syncing, drive, charging) = settings
            HomeMisc(
                syncing,
                session,
                connection,
                network,
                missing,
                drive,
                prefs.autoBackupEnabled,
                prefs.appLockEnabled,
                prefs.showArchivedFiles,
                prefs.showHiddenFiles,
                prefs.showRecentFiles,
                BackupGate.hold(prefs, network, charging)
            )
        }
    ) { countsWithStorage, recents, favorites, activeTransfers, misc ->
        val (counts, storage, lastBackupAt) = countsWithStorage
        val (syncing, session, connection, network, missing, drive) = misc
        val appLockEnabled = misc.appLockEnabled
        val showArchived = misc.showArchivedSection
        val showHidden = misc.showHiddenSection
        HomeUiState(
            loading = false,
            connection = connection,
            offline = network == NetworkStatus.UNAVAILABLE,
            totalFiles = counts.total,
            remoteBytes = counts.remoteBytes,
            backedUpCount = counts.backedUp,
            pendingCount = counts.pending,
            localOnlyCount = counts.localOnly,
            failedCount = counts.failed,
            recentFiles = recents,
            favoriteFolders = favorites,
            activeBackup = session,
            missingPermissions = missing,
            backupFoldersSelected = drive?.backupFolders?.isNotEmpty() == true,
            activeChannel = drive,
            storage = storage,
            autoBackupEnabled = misc.autoBackupEnabled,
            lastBackupAt = lastBackupAt,
            appLockEnabled = appLockEnabled,
            rebuilding = syncing,
            showArchivedSection = showArchived,
            showHiddenSection = showHidden,
            showRecentSection = misc.showRecentSection,
            activeTransferCount = activeTransfers,
            offlineBytes = counts.offlineBytes,
            backupHold = misc.backupHold
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    /** A folder scan never reaches files added by hand, or canceled or failed on their way up. */
    fun backUpPending() {
        if (_scanning.value) return
        viewModelScope.launch {
            _scanning.value = true
            val message = when (val result = transferRepository.enqueuePendingUploads()) {
                is AppResult.Success -> if (result.value == 0) {
                    UiText.Resource(Res.string.home_nothing_new_to_back_up)
                } else {
                    UiText.PluralResource(
                        Res.plurals.home_queued_files, result.value,
                        result.value
                    )
                }

                is AppResult.Failure -> result.error.toUiText()
            }
            _scanning.value = false
            _messages.tryEmit(message)
        }
    }

    val pendingSync: StateFlow<PendingSync> = combine(
        fileDao.observePendingPublishCount(),
        folderDao.observePendingPublishCount()
    ) { files, folders -> PendingSync(files, folders > 0) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PendingSync())

    fun syncNow() = publishScheduler.kick()

    val sections: StateFlow<HomeSections> = settingsRepository.preferences
        .map(HomeSections::of)
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeSections.of(UserPreferences()))

    fun claimFirstConnect(): Boolean = firstConnectClaimed.compareAndSet(false, true)

    private val lockedPromptDismissed = MutableStateFlow(false)

    val lockedFilesPrompt: StateFlow<Int> = combine(
        activeChannel.observe(),
        syncRepository.lockedFileCounts,
        lockedPromptDismissed
    ) { chatId, counts, dismissed ->
        if (dismissed || chatId == null) 0 else counts[chatId] ?: 0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    fun dismissLockedFilesPrompt() {
        lockedPromptDismissed.value = true
    }

    fun scanNow() {
        if (_scanning.value) return
        viewModelScope.launch {
            _scanning.value = true
            val message = when (val result = backupRepository.startBackup(BackupTrigger.MANUAL)) {
                is AppResult.Success ->
                    if (result.value == null) {
                        UiText.Resource(Res.string.home_nothing_new_to_back_up)
                    } else {
                        UiText.Resource(Res.string.home_backing_up_new_files)
                    }

                is AppResult.Failure -> result.error.toUiText()
            }
            _scanning.value = false
            _messages.tryEmit(message)
        }
    }

    fun pauseBackup(sessionId: String) {
        viewModelScope.launch { backupRepository.pauseBackup(sessionId) }
    }

    fun resumeBackup(sessionId: String) {
        viewModelScope.launch { backupRepository.resumeBackup(sessionId) }
    }

    fun cancelBackup(sessionId: String) {
        viewModelScope.launch { backupRepository.cancelBackup(sessionId) }
    }

    fun lockNow() = appLockManager.lockNow()

    fun refreshPermissions() {
        missingPermissions.value = permissionChecker.missingCritical()
    }

    private companion object {
        const val COUNTS_DEBOUNCE_MS = 300L
        val firstConnectClaimed = AtomicBoolean(false)
    }
}
