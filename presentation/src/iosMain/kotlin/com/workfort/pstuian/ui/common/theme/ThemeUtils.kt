package com.workfort.pstuian.ui.common.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
actual fun AppThemeSideEffect(
    isDark: Boolean,
    statusBarColor: Color,
    systemBarSyncKey: Any?,
) {
    // No-op for iOS as status bar is handled differently
}

@Composable
actual fun ApplySystemBarColors(
    statusBarColor: Color,
    statusBarDarkIcons: Boolean,
    navigationBarColor: Color,
    navigationBarDarkIcons: Boolean,
) {
    // No-op for iOS. The status bar is controlled by the hosting UIViewController.
}