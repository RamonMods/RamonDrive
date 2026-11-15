package com.ramon.ramondrive.presentation.home

import com.ramon.ramondrive.core.network.NetworkStatus
import com.ramon.ramondrive.core.permissions.AppPermission
import com.ramon.ramondrive.core.telegram.TelegramConnectionState
import com.ramon.ramondrive.domain.model.BackupHold
import com.ramon.ramondrive.domain.model.BackupSession
import com.ramon.ramondrive.domain.model.DriveChannel

internal data class HomeMisc(
    val syncing: Boolean,
    val session: BackupSession?,
    val connection: TelegramConnectionState,
    val network: NetworkStatus,
    val missing: List<AppPermission>,
    val activeDrive: DriveChannel?,
    val autoBackupEnabled: Boolean,
    val appLockEnabled: Boolean,
    val showArchivedSection: Boolean,
    val showHiddenSection: Boolean,
    val showRecentSection: Boolean,
    val backupHold: BackupHold?
)
