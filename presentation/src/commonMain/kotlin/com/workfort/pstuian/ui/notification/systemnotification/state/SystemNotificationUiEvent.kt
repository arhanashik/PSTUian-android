package com.workfort.pstuian.ui.notification.systemnotification.state

import com.workfort.pstuian.featuredomain.model.Notification

sealed interface SystemNotificationUiEvent {
    data class NotificationClicked(val notification: Notification.SystemNotification) : SystemNotificationUiEvent
}
