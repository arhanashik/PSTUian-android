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

    fun onClickBack() {
        _uiState.update { it.copy(navigationState = DeleteAccountUiState.NavigationState.GoBack) }
    }

    fun onClickDeleteAccountBtn() {
        _uiState.update { it.copy(messageState = DeleteAccountUiState.MessageState.ConfirmAccountDelete) }
    }

    fun onChangeInput(input: String) {
        _uiState.update {
            it.copy(
                input = input,
                validationError = validate(input),
            )
        }
    }

    fun onRequestRecovery() {
        _uiState.update {
            it.copy(
                messageState = null,
                navigationState = DeleteAccountUiState.NavigationState.ResetToContactUsScreen,
            )
        }
    }

    fun onResetToHomeScreen() {
        _uiState.update {
            it.copy(
                messageState = null,
                navigationState = DeleteAccountUiState.NavigationState.ResetToHomeScreen,
            )
        }
    }

    fun messageConsumed() {
        _uiState.update { it.copy(messageState = null) }
    }

    fun navigationConsumed() {
        _uiState.update { it.copy(navigationState = null) }
    }

    fun updateMessageState(messageState: DeleteAccountUiState.MessageState) {
        _uiState.update { it.copy(messageState = messageState) }
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
