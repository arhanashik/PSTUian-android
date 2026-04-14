package com.workfort.pstuian.ui.deleteaccount

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountUiEvent
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountUiState
import kotlinx.coroutines.launch

class DeleteAccountViewModel(
    private val authRepo: AuthRepository,
    private val stateMachine: DeleteAccountUiStateMachine,
) : UiStateMachineViewModel<DeleteAccountUiState>(stateMachine) {

    override fun onUiReady() {}

    fun onUiEvent(event: DeleteAccountUiEvent) {
        viewModelScope.launch {
            when (event) {
                is DeleteAccountUiEvent.OnClickBack -> stateMachine.onClickBack()
                is DeleteAccountUiEvent.OnClickDeleteAccountBtn -> stateMachine.onClickDeleteAccountBtn()
                is DeleteAccountUiEvent.OnChangeInput -> stateMachine.onChangeInput(event.input)
                is DeleteAccountUiEvent.OnDeleteAccount -> deleteAccount()
                is DeleteAccountUiEvent.OnRequestRecovery -> stateMachine.onRequestRecovery()
                is DeleteAccountUiEvent.OnResetToHomeScreen -> stateMachine.onResetToHomeScreen()
                is DeleteAccountUiEvent.MessageConsumed -> stateMachine.messageConsumed()
                is DeleteAccountUiEvent.NavigationConsumed -> stateMachine.navigationConsumed()
            }
        }
    }

    private suspend fun deleteAccount() {
        val currentState = stateMachine.uiState.value
        if (currentState.validationError.isNotEmpty()) return

        stateMachine.updateMessageState(DeleteAccountUiState.MessageState.Loading(cancelable = false))

        runCatching {
            authRepo.deleteAccount(password = currentState.input)
        }.onSuccess {
            stateMachine.updateMessageState(
                DeleteAccountUiState.MessageState.Success(
                    "Account deleted successfully!",
                )
            )
        }.onFailure {
            val message = it.message ?: "Failed to delete account. Please try again."
            stateMachine.updateMessageState(DeleteAccountUiState.MessageState.Error(message))
        }
    }
}
