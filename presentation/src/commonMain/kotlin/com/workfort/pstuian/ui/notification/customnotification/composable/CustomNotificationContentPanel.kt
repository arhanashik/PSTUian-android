package com.workfort.pstuian.ui.notification.customnotification.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.Notification
import com.workfort.pstuian.ui.common.composable.EmptyContentPanel
import com.workfort.pstuian.ui.notification.common.composable.DateHeader
import com.workfort.pstuian.ui.notification.common.displaydata.NotificationDisplayData
import com.workfort.pstuian.ui.notification.customnotification.state.CustomNotificationUiEvent
import com.workfort.pstuian.ui.notification.customnotification.state.CustomNotificationUiState

@Composable
internal fun CustomNotificationContentPanel(
    uiState: CustomNotificationUiState.Content,
    onUiEvent: (CustomNotificationUiEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (uiState.groupedNotifications.isEmpty() && uiState.isLoading) {
            CustomNotificationListShimmer()
            return
        }

        if (uiState.groupedNotifications.isEmpty()) {
            EmptyContentPanel(
                modifier = Modifier.fillMaxSize(),
                title = "No custom notifications",
                description = "You'll see activity and messages here.",
                icon = Icons.Default.NotificationsNone,
                actionButtonText = null,
            )
        } else {
            CustomNotificationListView(
                groupedNotifications = uiState.groupedNotifications,
                isContentLoading = uiState.isLoading,
                onUiEvent = onUiEvent,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CustomNotificationListView(
    groupedNotifications: Map<String, List<NotificationDisplayData>>,
    isContentLoading: Boolean,
    onUiEvent: (CustomNotificationUiEvent) -> Unit,
) {
    val listState = rememberLazyListState()
    var lastLoadMoreRequestedAtSize by remember { mutableIntStateOf(-1) }
    val totalItemCount = groupedNotifications.values.sumOf { it.size }

    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            if (totalItems == 0) return@derivedStateOf false
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: return@derivedStateOf false
            lastVisibleIndex >= totalItems - 1
        }
    }

    LaunchedEffect(shouldLoadMore, isContentLoading, totalItemCount) {
        val canRequestMore = shouldLoadMore && !isContentLoading && totalItemCount > 0
        if (canRequestMore && lastLoadMoreRequestedAtSize != totalItemCount) {
            lastLoadMoreRequestedAtSize = totalItemCount
            onUiEvent(CustomNotificationUiEvent.LoadMore)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        var cumulativeItemCount = 0
        groupedNotifications.forEach { (date, notifications) ->
            val headerDelay = (cumulativeItemCount * 50).toLong().coerceAtMost(500L)
            stickyHeader(key = date) {
                DateHeader(date, delay = headerDelay)
            }
            cumulativeItemCount++

            val groupStartCount = cumulativeItemCount
            itemsIndexed(
                items = notifications,
                key = { _, displayData -> displayData.notification.id },
            ) { index, displayData ->
                val itemDelay = ((groupStartCount + index) * 50).toLong().coerceAtMost(700L)
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    CustomNotificationItem(
                        displayData = displayData,
                        slideFromLeft = false,
                        delay = itemDelay,
                        onClick = {
                            (displayData.notification as? Notification.CustomNotification)?.let { n ->
                                onUiEvent(CustomNotificationUiEvent.NotificationClicked(n))
                            }
                        },
                    )
                }
            }
            cumulativeItemCount += notifications.size
        }

        if (isContentLoading) {
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    CustomNotificationShimmer()
                }
            }
        }
    }
}

@Composable
private fun CustomNotificationListShimmer() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
    ) {
        items(6) {
            Box(modifier = Modifier.padding(vertical = 6.dp)) {
                CustomNotificationShimmer()
            }
        }
    }
}
