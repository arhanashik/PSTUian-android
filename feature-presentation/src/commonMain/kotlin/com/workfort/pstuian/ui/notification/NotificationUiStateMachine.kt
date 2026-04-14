package com.workfort.pstuian.ui.notification

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.NotificationEntity
import com.workfort.pstuian.ui.notification.state.NotificationMessageState
import com.workfort.pstuian.ui.notification.state.NotificationNavigationState
import com.workfort.pstuian.ui.notification.state.NotificationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotificationUiStateMachine : UiStateMachine<NotificationUiState> {
    private val _uiState = MutableStateFlow(NotificationUiState())
    override val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    fun updateNotifications(notifications: List<NotificationEntity>, isLoading: Boolean) {
        _uiState.update {
            it.copy(
                notifications = notifications,
                isLoading = isLoading,
                error = null
            )
        }
    }

    fun updateError(error: String) {
        _uiState.update { it.copy(error = error, isLoading = false) }
    }

    fun showMessage(messageState: NotificationMessageState?) {
        _uiState.update { it.copy(messageState = messageState) }
    }

    fun navigateTo(navigationState: NotificationNavigationState?) {
        _uiState.update { it.copy(navigationState = navigationState) }
    }
}
