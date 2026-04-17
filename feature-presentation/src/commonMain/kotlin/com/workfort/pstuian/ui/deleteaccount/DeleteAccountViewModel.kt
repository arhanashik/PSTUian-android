package com.workfort.pstuian.ui.deleteaccount

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountMessageState
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountNavigationState
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountUiEvent
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeleteAccountViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val authRepo: AuthRepository,
    private val stateMachine: DeleteAccountUiStateMachine,
) : UiStateMachineViewModel<DeleteAccountUiState>(stateMachine) {

    private val _message = MutableStateFlow<DeleteAccountMessageState?>(null)
    val message: StateFlow<DeleteAccountMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<DeleteAccountNavigationState?>(null)
    val navigation: StateFlow<DeleteAccountNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {}

    fun onUiEvent(event: DeleteAccountUiEvent) {
        viewModelScope.launch {
            when (event) {
                is DeleteAccountUiEvent.OnClickBack -> onClickBack()
                is DeleteAccountUiEvent.OnClickDeleteAccountBtn -> onClickDeleteAccountBtn()
                is DeleteAccountUiEvent.OnChangeInput -> stateMachine.onChangeInput(event.input)
                is DeleteAccountUiEvent.OnDeleteAccount -> deleteAccount()
                is DeleteAccountUiEvent.OnRequestRecovery -> onRequestRecovery()
                is DeleteAccountUiEvent.OnResetToHomeScreen -> onResetToHomeScreen()
                is DeleteAccountUiEvent.MessageConsumed -> onMessageHandled()
                is DeleteAccountUiEvent.NavigationConsumed -> onNavigationConsumed()
            }
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationConsumed() = _navigation.update { null }

    private fun onClickBack() = _navigation.update { DeleteAccountNavigationState.GoBack }

    private fun onClickDeleteAccountBtn() {
        _message.update { DeleteAccountMessageState.ConfirmAccountDelete }
    }

    private fun onRequestRecovery() {
        _message.update { null }
        _navigation.update { DeleteAccountNavigationState.ResetToContactUsScreen }
    }

    private fun onResetToHomeScreen() {
        _message.update { null }
        _navigation.update { DeleteAccountNavigationState.ResetToHomeScreen }
    }

    private suspend fun deleteAccount() {
        val currentState = stateMachine.uiState.value
        if (currentState.validationError.isNotEmpty()) return

        _message.update { DeleteAccountMessageState.Loading(cancelable = false) }

        runCatching {
            authRepo.deleteAccount(password = currentState.input)
        }.onSuccess {
            _message.update {
                DeleteAccountMessageState.Success(
                    "Account deleted successfully!",
                )
            }
        }.onFailure {
            val message = it.message ?: "Failed to delete account. Please try again."
            _message.update { DeleteAccountMessageState.Error(message) }
        }
    }
}
