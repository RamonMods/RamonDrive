package com.ramon.ramondrive.presentation.settings

import com.ramon.ramondrive.core.telegram.TelegramConnectionState
import com.ramon.ramondrive.core.telegram.TelegramUser
import com.ramon.ramondrive.domain.model.UserPreferences
import com.ramon.ramondrive.domain.repository.CacheStats

data class SettingsUiState(
    val preferences: UserPreferences = UserPreferences(),
    val user: TelegramUser? = null,
    val connection: TelegramConnectionState = TelegramConnectionState.CONNECTING,
    val cacheStats: CacheStats = CacheStats(0, 0, 0, 0, 0),
    val syncing: Boolean = false,
    val loading: Boolean = true
)
