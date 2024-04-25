package com.workfort.pstuian.app.ui.common.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.workfort.pstuian.R

object AppFont {
    val Poppins = FontFamily(
        Font(R.font.poppins_regular),
        Font(R.font.poppins_italic, style = FontStyle.Italic),
        Font(R.font.poppins_semi_bold, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold),
    )
}

private val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = AppFont.Poppins),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = AppFont.Poppins),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = AppFont.Poppins),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = AppFont.Poppins),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = AppFont.Poppins),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = AppFont.Poppins),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = AppFont.Poppins),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = AppFont.Poppins),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = AppFont.Poppins),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = AppFont.Poppins),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = AppFont.Poppins),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = AppFont.Poppins),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = AppFont.Poppins),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = AppFont.Poppins),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = AppFont.Poppins)
)