package com.ramon.ramondrive.domain.repository

import com.ramon.ramondrive.core.common.AppResult

interface KeyBackupRepository {

    suspend fun createBackup(passphrase: CharArray, hint: String?): AppResult<Unit>

    suspend fun restore(passphrase: CharArray): AppResult<Boolean>

    /** Stored in plaintext, so it can be read without the passphrase. */
    suspend fun backupHint(): AppResult<String?>
}
