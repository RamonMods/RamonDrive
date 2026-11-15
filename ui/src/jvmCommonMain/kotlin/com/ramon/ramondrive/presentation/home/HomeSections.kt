package com.ramon.ramondrive.presentation.home

import com.ramon.ramondrive.domain.model.UserPreferences

data class HomeSections(
    val showRecent: Boolean,
    val showArchived: Boolean,
    val showHidden: Boolean
) {
    companion object {
        fun of(preferences: UserPreferences) = HomeSections(
            showRecent = preferences.showRecentFiles,
            showArchived = preferences.showArchivedFiles,
            showHidden = preferences.showHiddenFiles
        )
    }
}
