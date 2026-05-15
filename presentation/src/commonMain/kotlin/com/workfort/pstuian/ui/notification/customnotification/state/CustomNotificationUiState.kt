package com.workfort.pstuian.ui.notification.customnotification.state

import com.workfort.pstuian.ui.notification.common.displaydata.NotificationDisplayData

sealed interface CustomNotificationUiState {

    data object None : CustomNotificationUiState

    data class Content(
        val isLoading: Boolean = false,
        val groupedNotifications: Map<String, List<NotificationDisplayData>> = emptyMap(),
    ) : CustomNotificationUiState

    data class Error(val error: String? = null) : CustomNotificationUiState
}
