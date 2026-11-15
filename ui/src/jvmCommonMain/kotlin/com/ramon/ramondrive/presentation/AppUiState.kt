package com.ramon.ramondrive.presentation

import com.ramon.ramondrive.domain.model.AppTheme
import com.ramon.ramondrive.domain.model.AppLanguage

data class AppUiState(
    val loading: Boolean = true,
    val onboardingComplete: Boolean = false,
    val theme: AppTheme = AppTheme.SYSTEM,
    val language: AppLanguage = AppLanguage.SYSTEM,
    val dynamicColor: Boolean = true,
    val compactLayout: Boolean = false,
    val locked: Boolean = false
)
