package com.workfort.pstuian.ui.notification.common.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.theme.AppColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun DateHeader(date: String, delay: Long = 0) {
    val isPreview = LocalInspectionMode.current
    val animatedAlpha = remember { Animatable(if (isPreview) 1f else 0f) }
    val animatedOffset = remember { Animatable(if (isPreview) 0f else 20f) }

    LaunchedEffect(date) {
        if (isPreview) return@LaunchedEffect
        delay(delay)
        val duration = 300
        launch {
            animatedAlpha.animateTo(1f, animationSpec = tween(duration))
        }
        launch {
            animatedOffset.animateTo(0f, animationSpec = tween(duration, easing = EaseOutCubic))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.background)
            .graphicsLayer {
                alpha = animatedAlpha.value
                translationY = animatedOffset.value
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = date,
            style = MaterialTheme.typography.labelSmall.copy(
                color = AppColors.textTertiary,
            ),
            textAlign = TextAlign.Center,
        )
    }
}
