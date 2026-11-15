package com.ramon.ramondrive.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramon.ramondrive.core.security.AppLockManager
import com.ramon.ramondrive.core.telegram.TelegramAuthState
import com.ramon.ramondrive.domain.model.BackupTrigger
import com.ramon.ramondrive.domain.model.LayoutDensity
import com.ramon.ramondrive.domain.repository.BackupRepository
import com.ramon.ramondrive.domain.repository.ChannelRepository
import com.ramon.ramondrive.domain.repository.SettingsRepository
import com.ramon.ramondrive.domain.repository.SyncRepository
import com.ramon.ramondrive.domain.repository.TelegramAuthRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.ramon.ramondrive.core.update.AppRelease
import com.ramon.ramondrive.core.update.UpdateChecker
import com.ramon.ramondrive.presentation.update.UpdateController
import com.ramon.ramondrive.presentation.update.UpdateInstallState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.ramon.ramondrive.core.common.AppResult
import com.ramon.ramondrive.data.repository.LocalDataWiper
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class AppViewModel(
    private val settingsRepository: SettingsRepository,
    private val telegramAuthRepository: TelegramAuthRepository,
    private val syncRepository: SyncRepository,
    private val backupRepository: BackupRepository,
    private val channelRepository: ChannelRepository,
    private val appLockManager: AppLockManager,
    private val localDataWiper: LocalDataWiper,
    private val updateChecker: UpdateChecker,
    private val updateController: UpdateController
) : ViewModel() {

    private val _sessionBroken = MutableStateFlow(false)
    val sessionBroken: StateFlow<Boolean> = _sessionBroken.asStateFlow()

    private val _driveMissing = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val driveMissing = _driveMissing.asSharedFlow()

    private val _pendingUpdate = MutableStateFlow<AppRelease?>(null)
    val pendingUpdate: StateFlow<AppRelease?> = _pendingUpdate.asStateFlow()

    /** Stays set after the dialog is dismissed, so the update badge remains reachable. */
    private val _availableUpdate = MutableStateFlow<AppRelease?>(null)
    val availableUpdate: StateFlow<AppRelease?> = _availableUpdate.asStateFlow()

    val updateInstallState: StateFlow<UpdateInstallState> = updateController.state

    val canInstallInApp: Boolean get() = updateController.canInstallInApp

    @Volatile
    private var driveReady = false
    private var following: Job? = null

    val uiState: StateFlow<AppUiState> = combine(
        settingsRepository.preferences,
        appLockManager.locked
    ) { prefs, locked ->
        AppUiState(
            loading = false,
            onboardingComplete = prefs.onboardingComplete,
            theme = prefs.theme,
            language = prefs.language,
            dynamicColor = prefs.dynamicColor,
            compactLayout = prefs.layoutDensity == LayoutDensity.COMPACT,
            locked = locked
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, AppUiState())

    init {
        viewModelScope.launch {
            appLockManager.onAppStarted()
            backupRepository.refreshActiveSession()
            val started = telegramAuthRepository.startFromStoredCredentials()
            if (started is AppResult.Failure) _sessionBroken.value = true
        }
        checkForUpdate()
        combine(
            telegramAuthRepository.authState.map { it == TelegramAuthState.Ready },
            uiState.map { it.onboardingComplete }
        ) { ready, onboarded -> ready && onboarded }
            .distinctUntilChanged()
            .onEach { ready ->
                if (!ready) {
                    driveReady = false
                    stopFollowing()
                }
            }
            .filter { it }
            .onEach {
                channelRepository.refreshKnown()
                if (channelRepository.activeDriveMissing()) {
                    _driveMissing.tryEmit(Unit)
                    return@onEach
                }
                driveReady = true
                syncRepository.syncOnStart()
                catchUpBackup()
                viewModelScope.launch { syncRepository.catchUpWithRemote() }
                startFollowing()
            }
            .launchIn(viewModelScope)
    }

    /** Folders live with the active drive, so the check has to ask the channel. */
    private suspend fun catchUpBackup() {
        val chatId = settingsRepository.preferences.first().storageChatId ?: return
        if (channelRepository.backupFolders(chatId).isEmpty()) return
        backupRepository.startBackup(BackupTrigger.SCHEDULED)
    }

    fun onAppStopped() {
        appLockManager.onAppStopped()
        stopFollowing()
    }

    fun onAppStarted() {
        viewModelScope.launch { appLockManager.onAppStarted() }
        if (driveReady) {
            viewModelScope.launch { syncRepository.catchUpWithRemote() }
            startFollowing()
        }
    }

    private fun startFollowing() {
        if (following?.isActive == true) return
        following = viewModelScope.launch { syncRepository.followRemoteChanges() }
    }

    private fun stopFollowing() {
        following?.cancel()
        following = null
    }

    /**
     * Runs at most once a day whatever the notification setting says;
     * [force] is for the notification tap, which asks for the answer now.
     */
    fun checkForUpdate(force: Boolean = false) {
        viewModelScope.launch {
            val prefs = settingsRepository.preferences.first()
            if (!prefs.onboardingComplete) return@launch
            val elapsed = System.currentTimeMillis() - prefs.lastUpdateCheckAt
            if (!force && elapsed < UPDATE_CHECK_INTERVAL_MS) return@launch

            settingsRepository.update { it.copy(lastUpdateCheckAt = System.currentTimeMillis()) }
            val release = updateChecker.newerRelease()
            if (!force && release?.version == prefs.skippedUpdateVersion) return@launch
            _availableUpdate.value = release
            _pendingUpdate.value = release
        }
    }

    fun showUpdate() {
        _pendingUpdate.value = _availableUpdate.value
    }

    fun installUpdate(release: AppRelease) {
        updateController.install(viewModelScope, release)
    }

    fun resetUpdateInstall() {
        updateController.reset()
    }

    fun resetSession() {
        viewModelScope.launch {
            withContext(NonCancellable) {
                telegramAuthRepository.logout()
                localDataWiper.wipe()
                settingsRepository.clearTelegramCredentials()
                settingsRepository.update {
                    it.copy(
                        onboardingComplete = false,
                        storageChatId = null,
                        backupFolders = emptySet()
                    )
                }
            }
            _sessionBroken.value = false
        }
    }

    fun dismissUpdate() {
        _pendingUpdate.value = null
        updateController.reset()
    }

    fun unlock() = appLockManager.unlock()

    private companion object {
        const val UPDATE_CHECK_INTERVAL_MS = 24L * 60 * 60 * 1000
    }
}
