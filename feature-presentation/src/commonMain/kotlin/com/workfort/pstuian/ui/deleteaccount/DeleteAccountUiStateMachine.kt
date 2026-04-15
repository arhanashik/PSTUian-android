package com.workfort.pstuian.ui.deleteaccount

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DeleteAccountUiStateMachine : UiStateMachine<DeleteAccountUiState> {
    private val _uiState = MutableStateFlow(DeleteAccountUiState())
    override val uiState: StateFlow<DeleteAccountUiState> = _uiState.asStateFlow()

    fun onChangeInput(input: String) {
        _uiState.update {
            it.copy(
                input = input,
                validationError = validate(input),
            )
        }
    }

    private fun validate(password: String): String {
        return if (password.isEmpty()) {
            "*Required"
        } else if (password.length < 4) {
            "*Too short"
        } else {
            ""
        }
    }
}
