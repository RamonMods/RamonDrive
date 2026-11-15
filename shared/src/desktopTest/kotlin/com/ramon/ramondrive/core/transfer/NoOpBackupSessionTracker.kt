package com.ramon.ramondrive.core.transfer

internal class NoOpBackupSessionTracker : BackupSessionTracker {
    override suspend fun refreshActive() = Unit
    override suspend fun refresh(sessionId: String?) = Unit
}
