package com.workfort.pstuian.ui.notification.systemnotification.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.workfort.pstuian.featuredomain.model.Notification
import com.workfort.pstuian.ui.notification.common.composable.NotificationList
import com.workfort.pstuian.ui.notification.systemnotification.state.SystemNotificationUiEvent
import com.workfort.pstuian.ui.notification.systemnotification.state.SystemNotificationUiState

@Composable
internal fun SystemNotificationContentPanel(
    uiState: SystemNotificationUiState.Content,
    onUiEvent: (SystemNotificationUiEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        NotificationList(
            groupedNotifications = uiState.groupedNotifications,
            isLoading = uiState.isLoading,
            emptyTitle = "No system notifications",
            emptyDescription = "You'll see app updates and alerts here.",
            slideFromLeft = true,
            onNotificationClick = { notification ->
                if (notification is Notification.SystemNotification) {
                    onUiEvent(SystemNotificationUiEvent.NotificationClicked(notification))
                }
            },
        )
    }
}
