package com.ramon.ramondrive.domain.repository

import com.ramon.ramondrive.core.common.AppResult
import com.ramon.ramondrive.core.telegram.TelegramAuthState
import com.ramon.ramondrive.core.telegram.TelegramConnectionState
import com.ramon.ramondrive.core.telegram.TelegramCredentials
import com.ramon.ramondrive.core.telegram.TelegramUser
import com.ramon.ramondrive.domain.model.CountryList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface TelegramAuthRepository {

    val authState: StateFlow<TelegramAuthState>

    val connectionState: StateFlow<TelegramConnectionState>

    fun hasCredentials(): Flow<Boolean>

    suspend fun configure(credentials: TelegramCredentials): AppResult<Unit>

    suspend fun startFromStoredCredentials(): AppResult<Boolean>

    suspend fun countries(): AppResult<CountryList>

    suspend fun submitPhoneNumber(phoneNumber: String): AppResult<Unit>

    suspend fun requestQrCodeAuthentication(): AppResult<Unit>

    suspend fun restartAuthentication(): AppResult<Unit>

    suspend fun submitEmailAddress(email: String): AppResult<Unit>

    suspend fun submitEmailCode(code: String): AppResult<Unit>

    suspend fun submitCode(code: String): AppResult<Unit>

    suspend fun submitPassword(password: String): AppResult<Unit>

    suspend fun resendCode(): AppResult<Unit>

    suspend fun logout(): AppResult<Unit>

    suspend fun getCurrentUser(): AppResult<TelegramUser>
}
