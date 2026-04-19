package com.workfort.pstuian.ui.common.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SliderShimmer() {
    val peekWidth = 48.dp // 64dp padding - 16dp spacing
    val centerPadding = 64.dp

    Box(modifier = Modifier.fillMaxSize()) {
        // Left Peek
        Box(
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .width(peekWidth)
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp))
                .shimmerAnimation()
        )

        // Center Card
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = centerPadding)
                .clip(RoundedCornerShape(24.dp))
                .shimmerAnimation()
        )

        // Right Peek
        Box(
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .width(peekWidth)
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp))
                .shimmerAnimation()
        )
    }
}

@Composable
fun Modifier.shimmerAnimation(
    isLoading: Boolean = true,
    isDarkMode: Boolean = false,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
): Modifier {
    if (isLoading.not()) {
        return this
    }
    val shimmerColors = ShimmerAnimationData(isDarkMode = isDarkMode).getColours()
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
            val color = Color.White
            listOf(
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 1.0f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 0.3f),
            )
        }
    }
}