package com.workfort.pstuian.app.ui.notification

import com.workfort.pstuian.model.NotificationEntity

sealed class NotificationUiEvent {
    data object None : NotificationUiEvent()
    data object OnLoadMoreData : NotificationUiEvent()
    data object OnClickBack : NotificationUiEvent()
    data object OnClickRefresh : NotificationUiEvent()
    data class OnClickNotification(val notification: NotificationEntity) : NotificationUiEvent()
    data object MessageConsumed : NotificationUiEvent()
    data object NavigationConsumed : NotificationUiEvent()
}