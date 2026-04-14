package com.workfort.pstuian.ui.settings

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.ui.settings.state.SettingsUiEvent
import com.workfort.pstuian.ui.settings.state.SettingsUiState
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repo: SettingsRepository,
    private val stateMachine: SettingsUiStateMachine,
) : UiStateMachineViewModel<SettingsUiState>(stateMachine) {

    override fun onUiReady() {}

    fun onUiEvent(event: SettingsUiEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsUiEvent.OnClickBack -> stateMachine.onClickBack()
                is SettingsUiEvent.SetShowNotification -> setShowNotification(event.show)
                is SettingsUiEvent.MessageConsumed -> stateMachine.messageConsumed()
                is SettingsUiEvent.NavigationConsumed -> stateMachine.navigationConsumed()
            }
        }
    }

    private suspend fun setShowNotification(show: Boolean) {
        runCatching {
            repo.setShowNotification(show)
        }.onSuccess {
            stateMachine.setShowNotification(show)
        }.onFailure {
            val message = it.message ?: "Failed to change the settings"
            stateMachine.showError(message)
        }
    }
}
