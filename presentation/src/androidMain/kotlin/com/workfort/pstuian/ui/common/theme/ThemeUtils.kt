package com.workfort.pstuian.ui.common.theme

import android.app.Activity
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
actual fun AppThemeSideEffect(
    isDark: Boolean,
    statusBarColor: Color,
    systemBarSyncKey: Any?,
) {
    val view = LocalView.current
    if (view.isInEditMode) return
    val window = (view.context as Activity).window
    val controller = remember(view) { WindowCompat.getInsetsController(window, view) }
    val lifecycleOwner = LocalLifecycleOwner.current

    fun applyThemeStatusBar() {
        @Suppress("DEPRECATION")
        window.statusBarColor = statusBarColor.toArgb()
        controller.isAppearanceLightStatusBars = statusBarColor.luminance() > 0.5f
    }

    // Default themed status bar while no overriding screen runs: also reapply on resume so transient
    // window changes revert to theme when navigating back (screens that need custom bars use
    // ApplySystemBarColors and run after AppThemeSideEffect in the composition order).
    DisposableEffect(lifecycleOwner, statusBarColor, systemBarSyncKey) {
        applyThemeStatusBar()
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                applyThemeStatusBar()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    SideEffect {
        applyThemeStatusBar()
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
    val lifecycleOwner = LocalLifecycleOwner.current

    fun applyCurrentColors() {
        @Suppress("DEPRECATION")
        window.statusBarColor = statusBarColor.toArgb()
        @Suppress("DEPRECATION")
        window.navigationBarColor = navigationBarColor.toArgb()
        controller.isAppearanceLightStatusBars = statusBarDarkIcons
        controller.isAppearanceLightNavigationBars = navigationBarDarkIcons
    }

    // Intentionally no restore-on-dispose: snapshotting window.statusBarColor is unreliable with
    // enableEdgeToEdge() (transparent / theme attrs), and disposal order can run after the
    // destination we're returning to has already reapplied AppThemeSideEffect — restoring would
    // flash or stick the wrong color (e.g. white from styles.xml).

    // When returning to an existing destination from the back stack, the composable may become
    // visible again without a full recomposition; reapply colors on resume to avoid stale bars.
    DisposableEffect(
        lifecycleOwner,
        statusBarColor,
        statusBarDarkIcons,
        navigationBarColor,
        navigationBarDarkIcons,
    ) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                applyCurrentColors()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Re-apply on every composition so per-screen overrides win after [AppThemeSideEffect] applies
    // the default themed status/nav colors in the same frame.
    SideEffect {
        applyCurrentColors()
    }
}