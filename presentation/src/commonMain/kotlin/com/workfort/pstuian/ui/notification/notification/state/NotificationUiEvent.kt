package com.workfort.pstuian.ui.notification.notification.state

sealed interface NotificationUiEvent {
    data object BackClicked : NotificationUiEvent
    data class SelectTab(val index: Int) : NotificationUiEvent
}
