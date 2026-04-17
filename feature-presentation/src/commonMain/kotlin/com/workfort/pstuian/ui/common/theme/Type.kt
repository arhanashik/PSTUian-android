package com.workfort.pstuian.ui.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.poppins_bold
import pstuian.feature_presentation.generated.resources.poppins_italic
import pstuian.feature_presentation.generated.resources.poppins_regular
import pstuian.feature_presentation.generated.resources.poppins_semi_bold

object AppFont {
    @Composable
    fun Poppins() = FontFamily(
        Font(Res.font.poppins_regular),
        Font(Res.font.poppins_italic, style = FontStyle.Italic),
        Font(Res.font.poppins_semi_bold, FontWeight.SemiBold),
        Font(Res.font.poppins_bold, FontWeight.Bold),
    )
}

@Composable
fun getTypography(): Typography {
    val poppins = AppFont.Poppins()
    return Typography(
        displayLarge = MaterialTheme.typography.displayLarge.copy(fontFamily = poppins),
        displayMedium = MaterialTheme.typography.displayMedium.copy(fontFamily = poppins),
        displaySmall = MaterialTheme.typography.displaySmall.copy(fontFamily = poppins),

        headlineLarge = MaterialTheme.typography.headlineLarge.copy(fontFamily = poppins),
        headlineMedium = MaterialTheme.typography.headlineMedium.copy(fontFamily = poppins),
        headlineSmall = MaterialTheme.typography.headlineSmall.copy(fontFamily = poppins),

        titleLarge = MaterialTheme.typography.titleLarge.copy(fontFamily = poppins),
        titleMedium = MaterialTheme.typography.titleMedium.copy(fontFamily = poppins),
        titleSmall = MaterialTheme.typography.titleSmall.copy(fontFamily = poppins),

        bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontFamily = poppins),
        bodyMedium = MaterialTheme.typography.bodyMedium.copy(fontFamily = poppins),
        bodySmall = MaterialTheme.typography.bodySmall.copy(fontFamily = poppins),

        labelLarge = MaterialTheme.typography.labelLarge.copy(fontFamily = poppins),
        labelMedium = MaterialTheme.typography.labelMedium.copy(fontFamily = poppins),
        labelSmall = MaterialTheme.typography.labelSmall.copy(fontFamily = poppins)
    )
}