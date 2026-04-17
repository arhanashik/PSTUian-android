package com.workfort.pstuian.ui.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

object AppColors {
    // Colors from original Theme.kt
    val ColorPrimary = Color(0xFF008577)
    val ColorPrimaryDark = Color(0xFF00574B)
    val ColorAccent = Color(0xFFD81B60)

    private val Purple80 = Color(0xFF667EEA)
    private val PurpleGrey80 = Color(0xFF764BA2)
    private val Pink80 = Color(0xFFEFB8C8)

    private val Purple40 = Color(0xFF667EEA)
    private val PurpleGrey40 = Color(0xFF625b71)
    private val Pink40 = Color(0xFF7D5260)

    // Status colors
    val crimson = Color(0xFFDC143C)
    val blue = Color(0xFF2196F3)
    val green = Color(0xFF4CAF50)
    val yellow = Color(0xFFFFC107)
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
        primary = Purple40,
        secondary = PurpleGrey40,
        tertiary = Pink40,
        background = Color(0xFFF5F7FA),
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = Color(0xFF1C1B1F),
        onSurface = Color(0xFF1C1B1F),
        onSurfaceVariant = Color(0xFF4A5568),
        outline = Color(0xFF718096),
        outlineVariant = Color(0xFFCBD5E0)
    )

    val DarkColorScheme = darkColorScheme(
        primary = Purple80,
        secondary = PurpleGrey80,
        tertiary = Pink80,
        background = Color(0xFF1C1B1F),
        surface = Color(0xFF2D3748),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = Color(0xFFE6E1E5),
        onSurface = Color(0xFFE6E1E5),
        onSurfaceVariant = Color(0xFFCBD5E0),
        outline = Color(0xFFA0AEC0),
        outlineVariant = Color(0xFF4A5568)
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