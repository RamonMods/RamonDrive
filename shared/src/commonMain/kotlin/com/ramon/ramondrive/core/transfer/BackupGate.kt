package com.ramon.ramondrive.core.transfer

import com.ramon.ramondrive.core.network.NetworkStatus
import com.ramon.ramondrive.domain.model.BackupHold
import com.ramon.ramondrive.domain.model.UserPreferences

/** Applies to backup transfers only; uploads and downloads the user starts are never held. */
object BackupGate {

    fun hold(prefs: UserPreferences, network: NetworkStatus, charging: Boolean): BackupHold? = when {
        prefs.backupChargingOnly && !charging -> BackupHold.CHARGER
        prefs.backupWifiOnly && network != NetworkStatus.UNMETERED -> BackupHold.WIFI
        else -> null
    }
}
