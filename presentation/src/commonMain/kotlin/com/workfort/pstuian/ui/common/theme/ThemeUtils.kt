package com.workfort.pstuian.ui.common.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
expect fun AppThemeSideEffect(
    isDark: Boolean,
    statusBarColor: Color,
    systemBarSyncKey: Any? = null,
)

/**
 * Sets the system status bar and navigation bar colors for the screen while it is composed.
 * The theme's [AppThemeSideEffect] (and/or the next destination's own call) is responsible for
 * the default after this leaves composition — restoring a snapshot of window colors on dispose is
 * unsafe with edge-to-edge and Compose disposal ordering.
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