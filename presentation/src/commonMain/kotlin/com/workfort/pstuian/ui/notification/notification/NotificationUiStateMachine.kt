package com.workfort.pstuian.ui.notification.notification

import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.notification.notification.state.NotificationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotificationUiStateMachine : UiStateMachine<NotificationUiState> {

    private val _uiState = MutableStateFlow<NotificationUiState>(NotificationUiState.None)
    override val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    fun setContent(tabs: List<String>, selectedTab: Int) {
        _uiState.update {
            NotificationUiState.Content(
                tabs = tabs,
                selectedTab = selectedTab,
            )
        }
    }

    fun selectTab(index: Int) {
        _uiState.update { current ->
            when (current) {
                is NotificationUiState.Content -> current.copy(selectedTab = index)
                else -> current
            }
        }
    }
}
