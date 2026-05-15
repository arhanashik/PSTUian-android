package com.workfort.pstuian.ui.notification.systemnotification.state

import com.workfort.pstuian.ui.notification.common.displaydata.NotificationDisplayData

sealed interface SystemNotificationUiState {

    data object None : SystemNotificationUiState

    data object Loading : SystemNotificationUiState

    data class Content(
        val isLoading: Boolean = false,
        val groupedNotifications: Map<String, List<NotificationDisplayData>> = emptyMap(),
    ) : SystemNotificationUiState

    data class Error(val error: String? = null) : SystemNotificationUiState
}
