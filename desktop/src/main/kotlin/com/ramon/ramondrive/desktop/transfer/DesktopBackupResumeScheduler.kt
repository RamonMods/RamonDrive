package com.ramon.ramondrive.desktop.transfer

import com.ramon.ramondrive.core.transfer.BackupResumeScheduler

class DesktopBackupResumeScheduler : BackupResumeScheduler {
    override fun resumeWhen(chargingOnly: Boolean, wifiOnly: Boolean) = Unit
}
