package com.ramon.ramondrive.presentation.home

import com.ramon.ramondrive.domain.model.DriveChannel
import com.ramon.ramondrive.domain.model.UserPreferences

internal data class HomeSettings(
    val prefs: UserPreferences,
    val syncing: Boolean,
    val activeDrive: DriveChannel?,
    val charging: Boolean
)
