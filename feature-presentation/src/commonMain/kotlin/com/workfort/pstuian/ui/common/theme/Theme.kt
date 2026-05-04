package com.workfort.pstuian.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.workfort.pstuian.featuredomain.model.ThemeMode

@Composable
fun AppTheme(
    theme: ThemeMode = ThemeMode.System,
    /** When non-null and changes (e.g. nav back stack entry id), Android reapplies the themed status bar for in-app navigation. */
    systemBarSyncKey: Any? = null,
    /** When true, [AppThemeSideEffect] uses the theme's background color for the status bar (Splash, Profile). */
    useSchemeBackgroundForStatusBar: Boolean = false,
    content: @Composable () -> Unit,
) {
    val isDark = when (theme) {
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
        ThemeMode.System -> isSystemInDarkTheme()
    }

    val colorScheme = if (isDark) {
        AppColors.DarkColorScheme
    } else {
        AppColors.LightColorScheme
    }

    val statusBarColorForThemeEffect =
        if (useSchemeBackgroundForStatusBar) colorScheme.background else colorScheme.primary

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getTypography(),
        content = {
            // Compose SideEffects for children run before effects that appear later in this block.
            // Placing AppThemeSideEffect after `content()` matches the historic ordering where the
            // themed status bar won over ApplySystemBarColors for the status bar only (nav bar is
            // left to per-screen ApplySystemBarColors). `systemBarSyncKey` triggers reapply on nav.
            content()
            AppThemeSideEffect(
                isDark = isDark,
                statusBarColor = statusBarColorForThemeEffect,
                systemBarSyncKey = systemBarSyncKey,
            )
        },
    )
}