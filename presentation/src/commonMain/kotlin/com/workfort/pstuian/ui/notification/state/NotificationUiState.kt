package com.workfort.pstuian.ui.notification.state

import com.workfort.pstuian.featuredomain.model.NotificationEntity

data class NotificationUiState(
    val notifications: List<NotificationEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val messageState: NotificationMessageState? = null,
    val navigationState: NotificationNavigationState? = null,
)
