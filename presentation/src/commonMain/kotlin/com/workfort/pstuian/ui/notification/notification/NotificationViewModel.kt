package com.workfort.pstuian.ui.notification.notification

import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.notification.notification.state.NotificationNavigationState
import com.workfort.pstuian.ui.notification.notification.state.NotificationUiEvent
import com.workfort.pstuian.ui.notification.notification.state.NotificationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotificationViewModel(
    private val uiStateMachine: NotificationUiStateMachine,
) : UiStateMachineViewModel<NotificationUiState>(uiStateMachine) {

    private val _navigation = MutableStateFlow<NotificationNavigationState?>(null)
    val navigation: StateFlow<NotificationNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {
        uiStateMachine.setContent(
            tabs = listOf("System", "Custom"),
            selectedTab = 0,
        )
    }

    fun onUiEvent(event: NotificationUiEvent) {
        when (event) {
            is NotificationUiEvent.BackClicked -> _navigation.update { NotificationNavigationState.GoBack }
            is NotificationUiEvent.SelectTab -> uiStateMachine.selectTab(event.index)
        }
    }

    fun onNavigationConsumed() = _navigation.update { null }
}
