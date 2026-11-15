package com.ramon.ramondrive.core.logging

import com.ramon.ramondrive.core.common.SafeLog
import com.ramon.ramondrive.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class DebugLogController(
    private val settingsRepository: SettingsRepository,
    private val recorder: DebugLogRecorder
) {

    fun start(scope: CoroutineScope, alwaysVerbose: Boolean) {
        settingsRepository.preferences
            .map { it.debugLogging }
            .distinctUntilChanged()
            .onEach { enabled ->
                SafeLog.verbose = enabled || alwaysVerbose
                SafeLog.sink = recorder.takeIf { enabled }
                if (!enabled) recorder.clear()
            }
            .launchIn(scope)
    }
}
