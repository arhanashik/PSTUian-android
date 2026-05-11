package com.workfort.pstuian.ui.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import pstuian.presentation.generated.resources.Res
import pstuian.presentation.generated.resources.poppins_bold
import pstuian.presentation.generated.resources.poppins_italic
import pstuian.presentation.generated.resources.poppins_regular
import pstuian.presentation.generated.resources.poppins_semi_bold

@Composable
fun getTypography(): Typography {
    val fontFamily = FontFamily(
        Font(Res.font.poppins_regular),
        Font(Res.font.poppins_italic, style = FontStyle.Italic),
        Font(Res.font.poppins_semi_bold, FontWeight.SemiBold),
        Font(Res.font.poppins_bold, FontWeight.Bold),
    )

    return Typography(
        displayLarge = MaterialTheme.typography.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = MaterialTheme.typography.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = MaterialTheme.typography.displaySmall.copy(fontFamily = fontFamily),

        headlineLarge = MaterialTheme.typography.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = MaterialTheme.typography.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = MaterialTheme.typography.headlineSmall.copy(fontFamily = fontFamily),

        titleLarge = MaterialTheme.typography.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = MaterialTheme.typography.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = MaterialTheme.typography.titleSmall.copy(fontFamily = fontFamily),

        bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = MaterialTheme.typography.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = MaterialTheme.typography.bodySmall.copy(fontFamily = fontFamily),

        labelLarge = MaterialTheme.typography.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = MaterialTheme.typography.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = MaterialTheme.typography.labelSmall.copy(fontFamily = fontFamily),
    )
}