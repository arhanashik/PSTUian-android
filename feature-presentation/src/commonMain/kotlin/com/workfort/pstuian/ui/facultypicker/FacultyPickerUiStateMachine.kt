package com.workfort.pstuian.ui.facultypicker

import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.facultypicker.state.FacultyPickerNavigationState
import com.workfort.pstuian.ui.facultypicker.state.FacultyPickerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FacultyPickerUiStateMachine : UiStateMachine<FacultyPickerUiState> {
    private val _uiState = MutableStateFlow(FacultyPickerUiState())
    override val uiState: StateFlow<FacultyPickerUiState> = _uiState.asStateFlow()

    fun updatePanelState(panelState: FacultyPickerUiState.PanelState) {
        _uiState.update { it.copy(panelState = panelState, isLoading = false, error = null) }
    }

    fun updateLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun updateError(error: String) {
        _uiState.update { 
            it.copy(
                error = error, 
                isLoading = false,
                panelState = FacultyPickerUiState.PanelState.Error(error)
            ) 
        }
    }

    fun navigateTo(navigationState: FacultyPickerNavigationState?) {
        _uiState.update { it.copy(navigationState = navigationState) }
    }
}
