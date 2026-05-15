package com.workfort.pstuian.ui.notification.systemnotification

import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.notification.common.displaydata.NotificationDisplayData
import com.workfort.pstuian.ui.notification.systemnotification.state.SystemNotificationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SystemNotificationUiStateMachine : UiStateMachine<SystemNotificationUiState> {

    private val _uiState = MutableStateFlow<SystemNotificationUiState>(SystemNotificationUiState.None)
    override val uiState: StateFlow<SystemNotificationUiState> = _uiState.asStateFlow()

    fun showLoading() {
        _uiState.update { SystemNotificationUiState.Loading }
    }

    fun showContentLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is SystemNotificationUiState.Content -> current.copy(isLoading = isLoading)
                else -> SystemNotificationUiState.Content(isLoading = isLoading)
            }
        }
    }

    fun showNotifications(groupedNotifications: Map<String, List<NotificationDisplayData>>) {
        _uiState.update { current ->
            when (current) {
                is SystemNotificationUiState.Content -> current.copy(
                    groupedNotifications = groupedNotifications,
                    isLoading = false,
                )
                else -> SystemNotificationUiState.Content(
                    groupedNotifications = groupedNotifications,
                    isLoading = false,
                )
            }
        }
    }

    fun showError(error: String) {
        _uiState.update { SystemNotificationUiState.Error(error) }
    }
}
