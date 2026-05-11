package com.workfort.pstuian.ui.notification.state

import com.workfort.pstuian.featuredomain.model.NotificationEntity

sealed interface NotificationUiEvent {
    data object OnClickBack : NotificationUiEvent
    data class OnClickNotification(val notification: NotificationEntity) : NotificationUiEvent
    data object OnLoadMore : NotificationUiEvent
    data object OnRefresh : NotificationUiEvent
    data object MessageConsumed : NotificationUiEvent
    data object NavigationConsumed : NotificationUiEvent
}
