package com.ramon.ramondrive.core.transfer

interface BackupSessionTracker {

    suspend fun refreshActive()

    suspend fun refresh(sessionId: String?)
}
