package com.workfort.pstuian.ui.settings

import com.workfort.pstuian.ui.common.uistate.UiStateMachine
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

    fun setShowNotification(show: Boolean) {
        _uiState.update { it.copy(showNotification = show) }
    }
}
