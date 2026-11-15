package com.ramon.ramondrive.presentation.home

import com.ramon.ramondrive.presentation.components.ConnectionIndicator
import org.jetbrains.compose.resources.StringResource

internal data class ConnectionStatus(
    val indicator: ConnectionIndicator,
    val labelRes: StringResource
)
