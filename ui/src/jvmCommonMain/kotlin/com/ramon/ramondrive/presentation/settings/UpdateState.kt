package com.ramon.ramondrive.presentation.settings

import com.ramon.ramondrive.core.update.AppRelease

sealed interface UpdateState {
    data object Idle : UpdateState
    data object Checking : UpdateState
    data class Available(val release: AppRelease) : UpdateState
}
