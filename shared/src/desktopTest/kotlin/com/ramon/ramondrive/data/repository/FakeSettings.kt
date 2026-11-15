package com.ramon.ramondrive.data.repository

import com.ramon.ramondrive.domain.model.UserPreferences
import com.ramon.ramondrive.domain.repository.SettingsRepository
import com.ramon.ramondrive.testing.unused
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class FakeSettings(initial: UserPreferences) : SettingsRepository by unused() {
    private val state = MutableStateFlow(initial)
    override val preferences: Flow<UserPreferences> = state

    override suspend fun update(transform: (UserPreferences) -> UserPreferences) {
        state.value = transform(state.value)
    }
}
