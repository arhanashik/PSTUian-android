package com.workfort.pstuian.ui.notification.state

import com.workfort.pstuian.featuredomain.model.Notification

sealed interface NotificationMessageState {
    data class ShowSystemNotification(val notification: Notification.SystemNotification) : NotificationMessageState
    data class ShowAlert(val title: String = "Error", val message: String) : NotificationMessageState
    data class Snackbar(val message: String) : NotificationMessageState
}
