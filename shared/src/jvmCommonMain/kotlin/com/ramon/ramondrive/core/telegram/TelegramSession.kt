package com.ramon.ramondrive.core.telegram

import com.ramon.ramondrive.core.common.AppResult
import com.ramon.ramondrive.domain.repository.TelegramAuthRepository
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull

class TelegramSession(private val telegramAuthRepository: TelegramAuthRepository) {

    suspend fun ensureReady(): Boolean {
        if (telegramAuthRepository.authState.value == TelegramAuthState.Ready) return true
        val started = telegramAuthRepository.startFromStoredCredentials()
        if (started !is AppResult.Success || !started.value) return false
        return withTimeoutOrNull(READY_TIMEOUT_MS.milliseconds) {
            telegramAuthRepository.authState.first { it == TelegramAuthState.Ready }
        } != null
    }

    private companion object {
        const val READY_TIMEOUT_MS = 45_000L
    }
}
