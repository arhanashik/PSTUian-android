package com.workfort.pstuian.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.workfort.pstuian.featuredomain.model.ThemeMode

@Composable
fun AppTheme(
    themeMode: ThemeMode = ThemeMode.System,
    /** When non-null and changes (e.g. nav back stack entry id), Android reapplies the themed status bar for in-app navigation. */
    systemBarSyncKey: Any? = null,
    /** When true, [AppThemeSideEffect] uses the theme's background color for the status bar (Splash, Profile). */
    useSchemeBackgroundForStatusBar: Boolean = false,
    content: @Composable () -> Unit,
) {
    val isDark = when (themeMode) {
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
            // SideEffects run in composition order. Apply the themed defaults first so screens can
            // call [ApplySystemBarColors] after and overwrite status + navigation bar until they leave.
            AppThemeSideEffect(
                isDark = isDark,
                statusBarColor = statusBarColorForThemeEffect,
                systemBarSyncKey = systemBarSyncKey,
            )
            content()
        },
    )
}