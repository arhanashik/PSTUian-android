package com.workfort.pstuian.ui.settings

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.settings.state.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsUiStateMachine(
    initialShowNotification: Boolean,
) : UiStateMachine<SettingsUiState> {
    private val _uiState = MutableStateFlow(SettingsUiState(showNotification = initialShowNotification))
    override val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun onClickBack() {
        _uiState.update { it.copy(navigationState = SettingsUiState.NavigationState.GoBack) }
    }

    fun messageConsumed() {
        _uiState.update { it.copy(messageState = null) }
    }

    fun navigationConsumed() {
        _uiState.update { it.copy(navigationState = null) }
    }

    fun setShowNotification(show: Boolean) {
        _uiState.update { it.copy(showNotification = show) }
    }

    fun showError(message: String) {
        _uiState.update { it.copy(messageState = SettingsUiState.MessageState.Error(message)) }
    }
}
