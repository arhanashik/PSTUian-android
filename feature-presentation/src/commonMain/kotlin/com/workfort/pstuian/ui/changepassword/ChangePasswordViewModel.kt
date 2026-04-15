package com.workfort.pstuian.ui.changepassword

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.ChangePasswordInput
import com.workfort.pstuian.featuredomain.model.ChangePasswordInputError
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordMessageState
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordNavigationState
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiEvent
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ChangePasswordViewModel(
    private val authRepo: AuthRepository,
    private val uiStateMachine: ChangePasswordUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<ChangePasswordUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<ChangePasswordMessageState?>(null)
    val message: StateFlow<ChangePasswordMessageState?> = _message

    private val _navigation = MutableStateFlow<ChangePasswordNavigationState?>(null)
    val navigation: StateFlow<ChangePasswordNavigationState?> = _navigation

    override fun onUiReady() {
        uiStateMachine.setInitialContent()
    }

    fun onUiEvent(event: ChangePasswordUiEvent) {
        when (event) {
            is ChangePasswordUiEvent.BackClicked -> onClickBack()
            is ChangePasswordUiEvent.InputChanged -> onChangeInput(event.input)
            is ChangePasswordUiEvent.ChangePasswordClicked -> changePassword()
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        _navigation.update { ChangePasswordNavigationState.GoBack }
    }

    private fun onChangeInput(input: ChangePasswordInput) {
        uiStateMachine.updateInput(input, input.validate())
    }

    private fun changePassword() {
        val content = uiState.value as? ChangePasswordUiState.Content ?: return
        val input = content.input
        val validationError = input.validate()
        uiStateMachine.updateInput(input, validationError)

        if (validationError.isNotEmpty()) return

        uiStateMachine.showLoading(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                authRepo.changePassword(
                    oldPassword = input.oldPassword,
                    newPassword = input.newPassword,
                )
            }.onSuccess {
                uiStateMachine.showLoading(false)
                _message.update {
                    ChangePasswordMessageState.Success("Password changed successfully!")
                }
            }.onFailure {
                uiStateMachine.showLoading(false)
                val message = it.message ?: "Failed to change password. Please try again."
                _message.update { ChangePasswordMessageState.Error(message) }
            }
        }
    }

    private fun ChangePasswordInput.validate() = ChangePasswordInputError(
        oldPassword = if (oldPassword.isEmpty()) {
            "*Required"
        } else if (oldPassword.length < 4) {
            "*Too short"
        } else {
            ""
        },
        newPassword = if (newPassword.isEmpty()) {
            "*Required"
        } else if (newPassword.length < 4) {
            "*Too short"
        } else {
            ""
        },
        confirmPassword = if (confirmPassword.isEmpty()) {
            "*Required"
        } else if (confirmPassword.length < 4) {
            "*Too short"
        } else if (newPassword.isNotEmpty() && newPassword != confirmPassword) {
            "*Confirm password should be same as new password"
        } else {
            ""
        },
    )
}
