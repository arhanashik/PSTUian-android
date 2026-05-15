package com.workfort.pstuian.ui.notification

import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.notification.displaydata.NotificationDisplayData
import com.workfort.pstuian.ui.notification.state.NotificationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class NotificationUiStateMachine : UiStateMachine<NotificationUiState> {

    private val _state = MutableStateFlow(NotificationUiState())
    override val uiState: StateFlow<NotificationUiState> = _state.asStateFlow()

    private fun updateUiState(updater: NotificationUiState.() -> NotificationUiState) =
        _state.update(updater)

    fun showLoading(isLoading: Boolean) = updateUiState {
        copy(isLoading = isLoading)
    }

    fun selectTab(tabIndex: Int) = updateUiState {
        copy(selectedTabIndex = tabIndex)
    }

    fun updateSystemNotifications(groupedNotifications: Map<String, List<NotificationDisplayData>>) = updateUiState {
        copy(groupedSystemNotifications = groupedNotifications, isLoading = false)
    }

    fun updateCustomNotifications(groupedNotifications: Map<String, List<NotificationDisplayData>>) = updateUiState {
        copy(groupedCustomNotifications = groupedNotifications, isLoading = false)
    }
}
