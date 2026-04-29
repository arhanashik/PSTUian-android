package com.workfort.pstuian.ui.changepassword

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInput
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInputError
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordScreenPanel
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordMessageState
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordNavigationState
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiEvent
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiState
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ChangePasswordViewModel(
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository,
    private val screenData: SharedScreenData,
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
            is ChangePasswordUiEvent.PanelChanged -> uiStateMachine.transitionTo(event.panel)
            is ChangePasswordUiEvent.ResetEmailChanged -> uiStateMachine.updateResetEmail(event.email)
            is ChangePasswordUiEvent.SendPasswordResetClicked -> sendPasswordResetLink(event.email)
            is ChangePasswordUiEvent.ChangePasswordInputChanged -> onChangePasswordInput(event.input)
            is ChangePasswordUiEvent.ChangePasswordClicked -> onClickChangePassword(event.input)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        when (uiState.value) {
            is ChangePasswordUiState.ResetPassword ->
                uiStateMachine.transitionTo(ChangePasswordScreenPanel.ChangePassword)
            else -> _navigation.update { ChangePasswordNavigationState.GoBack }
        }
    }

    private fun onChangePasswordInput(input: ChangePasswordInput) {
        uiStateMachine.updateChangePasswordInput(input)
    }

    private fun onClickChangePassword(input: ChangePasswordInput) {
        val validationError = input.validate()
        uiStateMachine.updateChangePasswordInputError(validationError)

        if (validationError.hasError()) return

        if (input.oldPassword == input.newPassword) {
            _message.update { ChangePasswordMessageState.Error("Both passwords are same, nothing to change") }
            return
        }

        val email = screenData.getCurrentUser()?.email ?: return
        val userType = settingsRepository.getUserType() ?: return

        _message.update { ChangePasswordMessageState.Loader() }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            authRepository.changePassword(email, userType, input.oldPassword, input.newPassword)
                .onSuccess {
                    onMessageHandled()
                    _message.update { ChangePasswordMessageState.Success("Password changed successfully!") }
                }
                .onFailure { error ->
                    onMessageHandled()
                    val msg = error.message ?: "Failed to change password. Please try again."
                    _message.update { ChangePasswordMessageState.Error(msg) }
                }
        }
    }

    private fun sendPasswordResetLink(email: String) {
        val validationError = if (email.isEmpty()) "*Required" else ""
        uiStateMachine.updateResetEmailValidationError(validationError)
        if (validationError.isNotEmpty()) return

        if (screenData.getCurrentUser()?.email != email) {
            _message.update {
                ChangePasswordMessageState.Error("Please enter the valid email address for this account")
            }
            return
        }

        _message.update { ChangePasswordMessageState.Loader() }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            authRepository.resetPassword(email)
                .onSuccess {
                    onMessageHandled()
                    _message.update {
                        ChangePasswordMessageState.Success("Password reset link request has been sent")
                    }
                }
                .onFailure { error ->
                    onMessageHandled()
                    _message.update {
                        ChangePasswordMessageState.Error(error.message ?: "Failed the reset password. Please retry")
                    }
                }
        }
    }

    private fun ChangePasswordInput.validate() = ChangePasswordInputError(
        oldPassword = if (oldPassword.isEmpty()) {
            "*Required"
        } else if (oldPassword.length < 6) {
            "*Too short"
        } else {
            ""
        },
        newPassword = if (newPassword.isEmpty()) {
            "*Required"
        } else if (newPassword.length < 6) {
            "*Too short"
        } else {
            ""
        },
        confirmPassword = if (confirmPassword.isEmpty()) {
            "*Required"
        } else if (confirmPassword.length < 6) {
            "*Too short"
        } else if (newPassword.isNotEmpty() && newPassword != confirmPassword) {
            "*Confirm password should be same as new password"
        } else {
            ""
        },
    )
}
