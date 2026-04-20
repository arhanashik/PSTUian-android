package com.workfort.pstuian.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.workfort.pstuian.featuredomain.model.ThemeMode

@Composable
fun AppTheme(
    theme: ThemeMode = ThemeMode.System,
    content: @Composable () -> Unit,
) {
    val isDark = when (theme) {
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
        ThemeMode.System -> isSystemInDarkTheme()
    }

    MaterialTheme(
        colorScheme = if (isDark) {
            AppColors.DarkColorScheme
        } else {
            AppColors.LightColorScheme
        },
        typography = getTypography(),
        content = content,
    )

    AppThemeSideEffect(isDark)
}