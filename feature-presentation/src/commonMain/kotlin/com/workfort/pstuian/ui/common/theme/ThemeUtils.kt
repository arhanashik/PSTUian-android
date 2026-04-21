package com.workfort.pstuian.ui.common.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
expect fun AppThemeSideEffect(isDark: Boolean)

/**
 * Sets the system status bar and navigation bar colors for the current screen. The implementation
 * should also restore the previous values when the composable leaves the composition so screens
 * don't bleed their bar styling into others.
 *
 * @param statusBarColor background color of the status bar
 * @param statusBarDarkIcons whether the status bar icons should be rendered dark (for light
 *   backgrounds). Pass `false` when the background is dark.
 * @param navigationBarColor background color of the navigation bar
 * @param navigationBarDarkIcons same semantics as [statusBarDarkIcons] but for the nav bar.
 */
@Composable
expect fun ApplySystemBarColors(
    statusBarColor: Color,
    statusBarDarkIcons: Boolean,
    navigationBarColor: Color,
    navigationBarDarkIcons: Boolean,
)