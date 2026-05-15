package com.workfort.pstuian.ui.notification.common.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.Notification
import com.workfort.pstuian.ui.common.composable.EmptyContentPanel
import com.workfort.pstuian.ui.notification.common.displaydata.NotificationDisplayData

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NotificationList(
    groupedNotifications: Map<String, List<NotificationDisplayData>>,
    isLoading: Boolean,
    emptyTitle: String,
    emptyDescription: String,
    slideFromLeft: Boolean,
    onNotificationClick: (Notification) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (groupedNotifications.isEmpty() && !isLoading) {
        EmptyContentPanel(
            modifier = modifier,
            title = emptyTitle,
            description = emptyDescription,
            icon = Icons.Default.NotificationsNone,
            actionButtonText = null,
        )
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        if (isLoading) {
            items(6) {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    NotificationShimmer()
                }
            }
        } else {
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
                        NotificationItem(
                            displayData = displayData,
                            slideFromLeft = slideFromLeft,
                            delay = itemDelay,
                            onClick = { onNotificationClick(displayData.notification) },
                        )
                    }
                }
                cumulativeItemCount += notifications.size
            }
        }
    }
}
