package com.ramon.ramondrive.presentation.theme

import com.google.android.material.color.MaterialColors

internal actual fun harmonizeColor(argb: Int, primaryArgb: Int): Int =
    MaterialColors.harmonize(argb, primaryArgb)
