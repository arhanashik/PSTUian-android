package com.workfort.pstuian.ui.notification.customnotification

import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.notification.common.displaydata.NotificationDisplayData
import com.workfort.pstuian.ui.notification.customnotification.state.CustomNotificationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CustomNotificationUiStateMachine : UiStateMachine<CustomNotificationUiState> {

    private val _uiState = MutableStateFlow<CustomNotificationUiState>(CustomNotificationUiState.None)
    override val uiState: StateFlow<CustomNotificationUiState> = _uiState.asStateFlow()

    fun showContentLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is CustomNotificationUiState.Content -> current.copy(isLoading = isLoading)
                else -> CustomNotificationUiState.Content(isLoading = isLoading)
            }
        }
    }

    fun showNotifications(groupedNotifications: Map<String, List<NotificationDisplayData>>) {
        _uiState.update { current ->
            when (current) {
                is CustomNotificationUiState.Content -> current.copy(
                    groupedNotifications = groupedNotifications,
                    isLoading = false,
                )
                else -> CustomNotificationUiState.Content(
                    groupedNotifications = groupedNotifications,
                    isLoading = false,
                )
            }
        }
    }

    fun showError(error: String) {
        _uiState.update { CustomNotificationUiState.Error(error) }
    }
}
