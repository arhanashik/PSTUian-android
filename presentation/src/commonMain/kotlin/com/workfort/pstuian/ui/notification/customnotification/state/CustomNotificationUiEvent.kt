package com.workfort.pstuian.ui.notification.customnotification.state

import com.workfort.pstuian.featuredomain.model.Notification

sealed interface CustomNotificationUiEvent {
    data object LoadMore : CustomNotificationUiEvent
    data class NotificationClicked(val notification: Notification.CustomNotification) : CustomNotificationUiEvent
}
