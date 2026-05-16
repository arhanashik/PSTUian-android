package com.workfort.pstuian.ui.common.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

@Composable
fun Modifier.shimmerAnimation(
    isLoading: Boolean = true,
    /** When null, follows [MaterialTheme] surface so light cards get a visible gray shimmer. */
    isDarkMode: Boolean? = null,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
): Modifier {
    if (isLoading.not()) {
        return this
    }
    val themeSurfaceIsDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    val shimmerColors = ShimmerAnimationData(isDarkMode = isDarkMode ?: themeSurfaceIsDark).getColours()
    val transition = rememberInfiniteTransition(label = "")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )
    return this then Modifier.background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
            end = Offset(x = translateAnimation.value, y = angleOfAxisY),
        ),
    )
}

data class ShimmerAnimationData(private val isDarkMode: Boolean) {
    fun getColours(): List<Color> {
        return if (isDarkMode) {
            val color = Color.Black
            listOf(
                color.copy(alpha = 0.0f),
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.0f),
            )
        } else {
            // Dark gray shimmer so placeholders stay visible on white / light surfaces (e.g. M3 cards).
            val color = Color.Black
            listOf(
                color.copy(alpha = 0.06f),
                color.copy(alpha = 0.12f),
                color.copy(alpha = 0.2f),
                color.copy(alpha = 0.12f),
                color.copy(alpha = 0.06f),
            )
        }
    }
}