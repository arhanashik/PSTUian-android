package com.workfort.pstuian.ui.notification.notification.state

sealed interface NotificationUiState {

    data object None : NotificationUiState

    data class Content(
        val tabs: List<String> = listOf("System", "Custom"),
        val selectedTab: Int = 0,
    ) : NotificationUiState
}
