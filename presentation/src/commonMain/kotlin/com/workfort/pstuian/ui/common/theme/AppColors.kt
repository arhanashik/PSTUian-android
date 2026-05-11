package com.workfort.pstuian.ui.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

object AppColors {
    // Brand palette — shared across the app
    val BrandGreen = Color(0xFF0B3D2E)           // dark forest green, primary brand color
    val BrandGreenContainer = Color(0xFF1A5A43)  // lighter forest green, used for chips / circular backdrops
    val BrandGreenSoft = Color(0xFF7DB8A1)       // light sage, readable on dark surfaces
    val BrandYellow = Color(0xFFFFC107)          // accent yellow

    // Neutrals
    private val NeutralWhite = Color.White
    private val BodyDark = Color(0xFF1A1A1A)
    private val LabelDark = Color(0xFF3D3D3D)
    private val LineLight = Color(0xFFD9D9D9)

    // Dark-mode neutrals (no green tint)
    private val DarkPrimary = Color(0xFF121417)
    private val SurfaceDark = Color(0xFF2A3037)
    private val SurfaceDarkVariant = Color(0xFF353C44)
    private val BackgroundDark = Color(0xFF1B1F24)
    private val OnSurfaceDark = Color(0xFFF7F9FC)
    private val OnSurfaceVariantDark = Color(0xFFCDD4DE)
    private val OutlineDark = Color(0xFFA3ADBA)
    private val OutlineVariantDark = Color(0xFF3A4149)

    // Legacy aliases kept for backward compatibility with older call sites
    val ColorPrimary = BrandGreen
    val ColorPrimaryDark = Color(0xFF05251C)
    val ColorAccent = BrandYellow

    // Status colors
    val crimson = Color(0xFFDC143C)
    val blue = Color(0xFF2196F3)
    val green = Color(0xFF4CAF50)
    val yellow = BrandYellow
    val gray = Color(0xFFA9A9A9)

    // Themed colors using MaterialTheme.colorScheme
    val textPrimary: Color
        @Composable
        get() = MaterialTheme.colorScheme.onSurface

    val textSecondary: Color
        @Composable
        get() = MaterialTheme.colorScheme.onSurfaceVariant

    val textTertiary: Color
        @Composable
        get() = MaterialTheme.colorScheme.outline

    val primary: Color
        @Composable
        get() = MaterialTheme.colorScheme.primary

    val primaryVariant: Color
        @Composable
        get() = MaterialTheme.colorScheme.secondary

    val onPrimary: Color
        @Composable
        get() = MaterialTheme.colorScheme.onPrimary

    val background: Color
        @Composable
        get() = MaterialTheme.colorScheme.background

    val error: Color
        @Composable
        get() = MaterialTheme.colorScheme.error

    val divider: Color
        @Composable
        get() = MaterialTheme.colorScheme.outlineVariant

    val card: Color
        @Composable
        get() = MaterialTheme.colorScheme.surface

    val link: Color
        @Composable
        get() = MaterialTheme.colorScheme.primary

    val glassBorder: Color
        @Composable
        get() = if (MaterialTheme.colorScheme.surface.luminance() > 0.5f) {
            Color.Black.copy(alpha = 0.08f)
        } else {
            Color.White.copy(alpha = 0.15f)
        }

    val glassBackground: Color
        @Composable
        get() = if (MaterialTheme.colorScheme.surface.luminance() > 0.5f) {
            Color.White.copy(alpha = 0.94f)
        } else {
            Color(0xFF2D3748).copy(alpha = 0.96f)
        }

    val LightColorScheme = lightColorScheme(
        primary = BrandGreen,
        onPrimary = NeutralWhite,
        primaryContainer = BrandGreenContainer,
        onPrimaryContainer = NeutralWhite,
        secondary = BrandYellow,
        onSecondary = BrandGreen,
        secondaryContainer = BrandYellow,
        onSecondaryContainer = BrandGreen,
        tertiary = BrandGreenContainer,
        onTertiary = NeutralWhite,
        background = Color(0xFFF5F7FA),
        onBackground = BodyDark,
        surface = NeutralWhite,
        onSurface = BodyDark,
        surfaceVariant = Color(0xFFEEF1EE),
        onSurfaceVariant = LabelDark,
        outline = Color(0xFF718983),
        outlineVariant = LineLight,
    )

    val DarkColorScheme = darkColorScheme(
        primary = DarkPrimary,
        onPrimary = NeutralWhite,
        primaryContainer = SurfaceDarkVariant,
        onPrimaryContainer = NeutralWhite,
        secondary = Color(0xFF9AA2AE),
        onSecondary = DarkPrimary,
        secondaryContainer = Color(0xFF323840),
        onSecondaryContainer = Color(0xFFD7DCE3),
        tertiary = Color(0xFFC3CAD4),
        onTertiary = DarkPrimary,
        background = BackgroundDark,
        onBackground = OnSurfaceDark,
        surface = SurfaceDark,
        onSurface = OnSurfaceDark,
        surfaceVariant = SurfaceDarkVariant,
        onSurfaceVariant = OnSurfaceVariantDark,
        outline = OutlineDark,
        outlineVariant = OutlineVariantDark,
    )
}

object AppGradients {
    val Default = Brush.verticalGradient(
        colors = listOf(Color(0x88000000), Color(0xAA000000), Color(0x88000000))
    )

    val Gradient1 = Brush.linearGradient(
        colors = listOf(Color(0xFFA8C0FF), Color(0xFF3F2B96))
    )

    val Gradient2 = Brush.verticalGradient(
        colors = listOf(Color(0xFFFDD034), Color(0xFFD52D8B))
    )

    val Gradient3 = Brush.horizontalGradient(
        colors = listOf(Color(0xFF29323C), Color(0xFF485563))
    )

    val Gradient4 = Brush.horizontalGradient(
        colors = listOf(Color(0xFF8693AB), Color(0xFFBDD4E7))
    )

    val Gradient5 = Brush.verticalGradient(
        colors = listOf(Color(0xFF22E6B9), Color(0xFF7525B9))
    )

    val Gradient6 = Brush.verticalGradient(
        colors = listOf(Color(0xFF4646FF), Color(0xFFE92BFF))
    )

    val Gradient7 = Brush.horizontalGradient(
        colors = listOf(Color(0xFFBC4E9C), Color(0xFFF80759))
    )

    val Gradient8 = Brush.horizontalGradient(
        colors = listOf(Color(0xFF800080), Color(0xFFFFC0CB))
    )

    val Gradient9 = Brush.linearGradient(
        colors = listOf(Color(0xFF1A2A6C), Color(0xFFB21F1F), Color(0xFFFDBB2D))
    )

    val Gradient10 = Brush.linearGradient(
        colors = listOf(Color(0xFFD7E1EC), Color(0xFFFFFFFF))
    )
}