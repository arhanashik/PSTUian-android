package com.workfort.pstuian.ui.common.theme

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
actual fun AppThemeSideEffect(
    isDark: Boolean,
    statusBarColor: Color,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            @Suppress("DEPRECATION")
            window.statusBarColor = statusBarColor.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                statusBarColor.luminance() > 0.5f
        }
    }
}

@Composable
actual fun ApplySystemBarColors(
    statusBarColor: Color,
    statusBarDarkIcons: Boolean,
    navigationBarColor: Color,
    navigationBarDarkIcons: Boolean,
) {
    val view = LocalView.current
    if (view.isInEditMode) return
    val window = (view.context as Activity).window
    val controller = remember(view) { WindowCompat.getInsetsController(window, view) }

    // Capture the values present when this composable entered the hierarchy so we can restore
    // them when it leaves. Keyed on `Unit` so the snapshot is taken exactly once per lifetime.
    DisposableEffect(Unit) {
        @Suppress("DEPRECATION")
        val previousStatusBarColor = window.statusBarColor
        @Suppress("DEPRECATION")
        val previousNavigationBarColor = window.navigationBarColor
        val previousStatusBarDarkIcons = controller.isAppearanceLightStatusBars
        val previousNavigationBarDarkIcons = controller.isAppearanceLightNavigationBars

        onDispose {
            @Suppress("DEPRECATION")
            window.statusBarColor = previousStatusBarColor
            @Suppress("DEPRECATION")
            window.navigationBarColor = previousNavigationBarColor
            controller.isAppearanceLightStatusBars = previousStatusBarDarkIcons
            controller.isAppearanceLightNavigationBars = previousNavigationBarDarkIcons
        }
    }

    // Re-apply on every composition so the values win over any ancestor side effects (e.g. the
    // theme-wide status bar styling) that may run in the same pass.
    SideEffect {
        @Suppress("DEPRECATION")
        window.statusBarColor = statusBarColor.toArgb()
        @Suppress("DEPRECATION")
        window.navigationBarColor = navigationBarColor.toArgb()
        controller.isAppearanceLightStatusBars = statusBarDarkIcons
        controller.isAppearanceLightNavigationBars = navigationBarDarkIcons
    }
}