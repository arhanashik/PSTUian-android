package com.workfort.pstuian.ui.notification.state

import com.workfort.pstuian.featuredomain.model.Notification

sealed interface NotificationUiEvent {
    data object BackClicked : NotificationUiEvent
    data class TabSelected(val tabIndex: Int) : NotificationUiEvent
    data class NotificationClicked(val notification: Notification) : NotificationUiEvent
}
