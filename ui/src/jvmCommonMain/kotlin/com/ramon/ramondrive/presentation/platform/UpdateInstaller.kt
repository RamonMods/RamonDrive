package com.ramon.ramondrive.presentation.platform

import com.ramon.ramondrive.core.update.AppRelease

/**
 * Downloads the matching installer asset and hands it to the operating system, so the user only
 * confirms the install. Desktop opens the .msi/.dmg/.deb and depends on the OS toolchain.
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
