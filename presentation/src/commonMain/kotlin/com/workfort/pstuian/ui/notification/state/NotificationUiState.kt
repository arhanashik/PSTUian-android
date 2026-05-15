package com.workfort.pstuian.ui.notification.state

import com.workfort.pstuian.ui.notification.displaydata.NotificationDisplayData

data class NotificationUiState(
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = 0,
    val groupedSystemNotifications: Map<String, List<NotificationDisplayData>> = emptyMap(),
    val groupedCustomNotifications: Map<String, List<NotificationDisplayData>> = emptyMap(),
)
