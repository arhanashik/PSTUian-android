package com.workfort.pstuian.ui.notification.systemnotification.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.notification.common.composable.notificationCardElevation
import com.workfort.pstuian.ui.notification.common.displaydata.NotificationDisplayData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun SystemNotificationItem(
    displayData: NotificationDisplayData,
    slideFromLeft: Boolean,
    delay: Long = 0,
    onClick: () -> Unit,
) {
    val isPreview = LocalInspectionMode.current
    val animatedAlpha = remember { Animatable(if (isPreview) 1f else 0f) }
    val isUnread = displayData.notification.readAt == 0L

    val initialOffset = if (slideFromLeft) -50f else 50f
    val animatedOffset = remember { Animatable(if (isPreview) 0f else initialOffset) }

    LaunchedEffect(Unit) {
        if (isPreview) return@LaunchedEffect

        delay(delay)
        val duration = 300
        val easing = EaseOutCubic

        launch {
            animatedAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(duration),
            )
        }
        launch {
            animatedOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(duration, easing = easing),
            )
        }
    }

    val backgroundColor = if (isUnread) {
        AppColors.card
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = animatedAlpha.value
                translationX = animatedOffset.value
            }
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = notificationCardElevation(isUnread),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = displayData.notification.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (isUnread) FontWeight.Bold else FontWeight.Normal,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = displayData.formattedTime,
                        style = MaterialTheme.typography.labelSmall.copy(color = AppColors.textTertiary),
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = displayData.notification.body,
                    style = MaterialTheme.typography.bodyMedium.copy(color = AppColors.textSecondary),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
