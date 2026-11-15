package com.ramon.ramondrive.core.transfer

interface BackupResumeScheduler {
    fun resumeWhen(chargingOnly: Boolean, wifiOnly: Boolean)
}
