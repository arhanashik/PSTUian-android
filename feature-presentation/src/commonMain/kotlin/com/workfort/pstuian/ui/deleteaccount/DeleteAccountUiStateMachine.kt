package com.workfort.pstuian.ui.deleteaccount

import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DeleteAccountUiStateMachine : UiStateMachine<DeleteAccountUiState> {
    private val _uiState = MutableStateFlow(DeleteAccountUiState())
    override val uiState: StateFlow<DeleteAccountUiState> = _uiState.asStateFlow()

    fun onChangeInput(input: String) {
        _uiState.update { it.copy(input = input) }
    }

    fun showValidationError(validationError: String) {
        _uiState.update { it.copy(validationError = validationError) }
    }
}
