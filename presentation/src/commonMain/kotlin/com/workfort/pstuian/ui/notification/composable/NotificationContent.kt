package com.workfort.pstuian.ui.notification.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.Notification
import com.workfort.pstuian.ui.common.composable.EmptyContentPanel
import com.workfort.pstuian.ui.common.composable.ToggleSwitch
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.notification.displaydata.NotificationDisplayData
import com.workfort.pstuian.ui.notification.state.NotificationUiEvent
import com.workfort.pstuian.ui.notification.state.NotificationUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NotificationContent(
    uiState: NotificationUiState,
    onUiEvent: (NotificationUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val tabOptions = listOf("System", "Custom")
    val pagerState = rememberPagerState(pageCount = { tabOptions.size })

    LaunchedEffect(pagerState.currentPage) {
        onUiEvent(NotificationUiEvent.TabSelected(tabIndex = pagerState.currentPage))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ToggleSwitch(
            options = tabOptions,
            selectedIndex = uiState.selectedTabIndex,
            modifier = Modifier.padding(16.dp),
            onSelectedIndexChange = { index ->
                scope.launch { pagerState.animateScrollToPage(index) }
            },
        )

        Box(modifier = Modifier.fillMaxSize().weight(1f)) {
            if (uiState.groupedSystemNotifications.isEmpty() && !uiState.isLoading) {
                EmptyContentPanel(
                    title = "No notifications yet",
                    description = "You'll see updates and alerts here.",
                    icon = Icons.Default.NotificationsNone,
                    actionButtonText = null,
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                ) {
                    if (uiState.isLoading) {
                        items(6) {
                            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                                NotificationShimmer()
                            }
                        }
                    } else {
                        var cumulativeItemCount = 0
                        uiState.groupedSystemNotifications.forEach { (date, notifications) ->
                            val headerDelay = (cumulativeItemCount * 50).toLong().coerceAtMost(500L)
                            stickyHeader(key = date) {
                                DateHeader(date, delay = headerDelay)
                            }
                            cumulativeItemCount++

                            val groupStartCount = cumulativeItemCount
                            itemsIndexed(
                                items = notifications,
                                key = { _, displayData -> displayData.notification.id }
                            ) { index, displayData ->
                                val itemDelay = ((groupStartCount + index) * 50).toLong().coerceAtMost(700L)
                                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                                    NotificationItem(
                                        displayData = displayData,
                                        delay = itemDelay,
                                        onClick = {
                                            onUiEvent(
                                                NotificationUiEvent.NotificationClicked(displayData.notification)
                                            )
                                        },
                                    )
                                }
                            }
                            cumulativeItemCount += notifications.size
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DateHeader(date: String, delay: Long = 0) {
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

@Composable
private fun NotificationItem(
    displayData: NotificationDisplayData,
    delay: Long = 0,
    onClick: () -> Unit
) {
    val isPreview = LocalInspectionMode.current
    val animatedAlpha = remember { Animatable(if (isPreview) 1f else 0f) }
    
    // System notification items enter from left (-50f), Custom enter from right (50f)
    val initialOffset = if (displayData.notification is Notification.SystemNotification) -50f else 50f
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

    val backgroundColor = if (displayData.formattedReadAt.isEmpty()) {
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
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (displayData.formattedReadAt.isEmpty()) 1.dp else 3.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            if (!displayData.formattedReadAt.isEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(AppColors.primary)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = displayData.notification.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (displayData.formattedReadAt.isEmpty()) FontWeight.Normal else FontWeight.Bold,
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
