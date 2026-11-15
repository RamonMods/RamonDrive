package com.ramon.ramondrive.presentation.platform

import com.ramon.ramondrive.core.update.AppRelease

/**
 * Hands a release APK to the platform installer. Desktop keeps the release page as the only path,
 * so [supported] is false there.
 */
interface UpdateInstaller {

    val supported: Boolean

    /**
     * Downloads [release] and opens the system installer.
     *
     * [onProgress] receives 0..1, or null while the total size is unknown.
     */
    suspend fun downloadAndInstall(
        release: AppRelease,
        onProgress: (Float?) -> Unit
    ): InstallOutcome
}

sealed interface InstallOutcome {
    data object InstallerOpened : InstallOutcome
    data object PermissionRequired : InstallOutcome
    data object Unsupported : InstallOutcome
    data class Failed(val reason: String? = null) : InstallOutcome
}

/** Desktop builds update through their package manager, so the button stays off. */
object NoopUpdateInstaller : UpdateInstaller {
    override val supported = false

    override suspend fun downloadAndInstall(
        release: AppRelease,
        onProgress: (Float?) -> Unit
    ): InstallOutcome = InstallOutcome.Unsupported
}
