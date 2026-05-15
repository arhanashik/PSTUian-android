package com.workfort.pstuian.ui.notification.systemnotification.state

import com.workfort.pstuian.featuredomain.model.Notification

sealed interface SystemNotificationMessageState {
    data class ShowDetail(val notification: Notification.SystemNotification) : SystemNotificationMessageState
}
