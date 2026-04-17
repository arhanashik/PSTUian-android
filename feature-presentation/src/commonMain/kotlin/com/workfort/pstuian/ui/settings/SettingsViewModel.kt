package com.workfort.pstuian.ui.settings

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.settings.state.SettingsMessageState
import com.workfort.pstuian.ui.settings.state.SettingsNavigationState
import com.workfort.pstuian.ui.settings.state.SettingsUiEvent
import com.workfort.pstuian.ui.settings.state.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repo: SettingsRepository,
    private val stateMachine: SettingsUiStateMachine,
) : UiStateMachineViewModel<SettingsUiState>(stateMachine) {

    private val _message = MutableStateFlow<SettingsMessageState?>(null)
    val message: StateFlow<SettingsMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<SettingsNavigationState?>(null)
    val navigation: StateFlow<SettingsNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {}

    fun onUiEvent(event: SettingsUiEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsUiEvent.OnClickBack -> onClickBack()
                is SettingsUiEvent.SetShowNotification -> setShowNotification(event.show)
                is SettingsUiEvent.MessageConsumed -> onMessageHandled()
                is SettingsUiEvent.NavigationConsumed -> onNavigationHandled()
            }
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        _navigation.update { SettingsNavigationState.GoBack }
    }

    private suspend fun setShowNotification(show: Boolean) {
        runCatching {
            repo.setShowNotification(show)
        }.onSuccess {
            stateMachine.setShowNotification(show)
        }.onFailure {
            val message = it.message ?: "Failed to change the settings"
            _message.update { SettingsMessageState.Error(message) }
        }
    }
}
