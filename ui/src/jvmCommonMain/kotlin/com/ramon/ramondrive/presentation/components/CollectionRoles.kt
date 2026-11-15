package com.ramon.ramondrive.presentation.components

import androidx.compose.ui.semantics.SemanticsPropertyReceiver

expect fun SemanticsPropertyReceiver.collectionRole(name: String, grid: Boolean)

expect fun SemanticsPropertyReceiver.tileRole()
