package com.ramon.ramondrive.presentation.update

import com.ramon.ramondrive.core.update.AppRelease
import com.ramon.ramondrive.presentation.platform.InstallOutcome
import com.ramon.ramondrive.presentation.platform.UpdateInstaller
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UpdateInstallState {
    data object Idle : UpdateInstallState

    data class Downloading(val progress: Float?) : UpdateInstallState

    data object InstallerOpened : UpdateInstallState

    data object PermissionRequired : UpdateInstallState

    data object Unsupported : UpdateInstallState

    data class Failed(val reason: String?) : UpdateInstallState
}

/**
 * One install at a time, shared by the app shell and the About screen so both show the same
 * progress instead of racing two downloads.
 */
class UpdateController(private val installer: UpdateInstaller) {

    private val _state = MutableStateFlow<UpdateInstallState>(UpdateInstallState.Idle)
    val state: StateFlow<UpdateInstallState> = _state.asStateFlow()

    private var job: Job? = null

    val canInstallInApp: Boolean get() = installer.supported

    fun install(scope: CoroutineScope, release: AppRelease) {
        if (job?.isActive == true) return
        if (!installer.supported || release.downloadUrl == null) {
            _state.value = UpdateInstallState.Unsupported
            return
        }
        job = scope.launch {
            _state.value = UpdateInstallState.Downloading(null)
            val outcome = installer.downloadAndInstall(release) { progress ->
                _state.value = UpdateInstallState.Downloading(progress)
            }
            _state.value = when (outcome) {
                InstallOutcome.InstallerOpened -> UpdateInstallState.InstallerOpened
                InstallOutcome.PermissionRequired -> UpdateInstallState.PermissionRequired
                InstallOutcome.Unsupported -> UpdateInstallState.Unsupported
                is InstallOutcome.Failed -> UpdateInstallState.Failed(outcome.reason)
            }
        }
    }

    fun reset() {
        job?.cancel()
        job = null
        _state.value = UpdateInstallState.Idle
    }
}
