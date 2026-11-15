package com.ramon.ramondrive.presentation.theme

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ramon.ramondrive.domain.model.AppTheme

/** OpenCode themes ship both a dark and a light palette; they follow the system appearance. */
fun AppTheme.resolveDarkTheme(systemDark: Boolean): Boolean = when (this) {
    AppTheme.LIGHT -> false
    AppTheme.DARK, AppTheme.AMOLED -> true
    else -> systemDark
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RamonDriveTheme(
    theme: AppTheme = AppTheme.SYSTEM,
    darkTheme: Boolean = systemDarkTheme(),
    dynamicColor: Boolean = true,
    amoled: Boolean = false,
    content: @Composable () -> Unit
) {
    val openCode = openCodeColorScheme(theme, darkTheme)
    val base = openCode
        ?: platformColorScheme(darkTheme, dynamicColor)
        ?: if (darkTheme) BlueDarkColorScheme else BlueLightColorScheme

    // Pure black keeps OLED panels from lighting unused pixels.
    val colorScheme = if (amoled && darkTheme && openCode == null) {
        base.copy(
            background = Color.Black,
            surface = Color.Black,
            surfaceDim = Color.Black,
            surfaceContainerLowest = Color.Black,
            surfaceContainerLow = Color.Black,
            surfaceContainer = Color.Black,
            surfaceContainerHigh = Color(0xFF0B0B0B),
            surfaceContainerHighest = Color(0xFF151515)
        )
    } else {
        base
    }

    PlatformThemeSideEffects(darkTheme)

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        motionScheme = MotionScheme.expressive(),
        typography = RamonDriveTypography,
        shapes = RamonDriveShapes,
        content = content
    )
}
