package com.ramon.ramondrive.presentation.components

import androidx.compose.runtime.Composable

internal data class GroupedListItem(
    val visible: Boolean,
    val content: @Composable () -> Unit
)
