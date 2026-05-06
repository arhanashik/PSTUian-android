package com.workfort.pstuian.ui.deleteaccount

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.model.SharedScreenData
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
    private val authRepo: AuthRepository,
    private val sharedScreenData: SharedScreenData,
    private val stateMachine: DeleteAccountUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<DeleteAccountUiState>(stateMachine) {

    private val _message = MutableStateFlow<DeleteAccountMessageState?>(null)
    val message: StateFlow<DeleteAccountMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<DeleteAccountNavigationState?>(null)
    val navigation: StateFlow<DeleteAccountNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {}

    fun onUiEvent(event: DeleteAccountUiEvent) {
        viewModelScope.launch {
            when (event) {
                is DeleteAccountUiEvent.BackClicked -> _navigation.update { DeleteAccountNavigationState.GoBack }
                is DeleteAccountUiEvent.OnChangeInput -> stateMachine.onChangeInput(event.input)
                is DeleteAccountUiEvent.DeactivateAccountClicked -> onClickDeactivateAccount(event.password)
                is DeleteAccountUiEvent.DeleteAccountClicked -> onClickRequestDeleteAccount()
            }
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationConsumed() = _navigation.update { null }

    private fun onClickDeactivateAccount(password: String) {
        val validation = validate(password)
        stateMachine.showValidationError(validation)
        if (!validation.isBlank()) return

        _message.update {
            DeleteAccountMessageState.ConfirmAction("Are you surely want to deactivate your account?") {
                deactivateAccount(password)
            }
        }
    }

    private fun onClickRequestDeleteAccount() {
        _message.update {
            DeleteAccountMessageState.ConfirmAction("Are you surely want to delete your account?") {
                sharedScreenData.getAppConfig()?.deleteAccountUrl?.let { url ->
                    _navigation.update { DeleteAccountNavigationState.OpenUrl(url) }
                }
            }
        }
    }

    private fun deactivateAccount(password: String) {
        val email = sharedScreenData.getCurrentUser()?.email ?: return
        val userType = sharedScreenData.getCurrentUserType() ?: return

        _message.update { DeleteAccountMessageState.Loading(cancelable = false) }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            authRepo.deactivateAccount(userType, email, password).onSuccess {
                onMessageHandled()
                _navigation.update { DeleteAccountNavigationState.ResetToHomeScreen }
            }.onFailure {
                onMessageHandled()
                val message = it.message ?: "Failed to deactivate account. Please try again."
                _message.update { DeleteAccountMessageState.Error(message) }
            }
        }
    }

    private fun validate(password: String): String {
        return if (password.isEmpty()) {
            "*Required"
        } else if (password.length < 6) {
            "*Too short"
        } else {
            ""
        }
    }
}
