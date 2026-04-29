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
    private val authRepo: AuthRepository,
    private val settingsRepository: SettingsRepository,
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
            is ChangePasswordUiEvent.PanelChanged -> uiStateMachine.setActivePanel(event.panel)
            is ChangePasswordUiEvent.ResetEmailChanged -> uiStateMachine.updateResetEmail(event.email)
            is ChangePasswordUiEvent.SendPasswordResetClicked -> sendPasswordResetLink()
            is ChangePasswordUiEvent.InputChanged -> onChangeInput(event.input)
            is ChangePasswordUiEvent.ChangePasswordClicked -> changePassword()
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        val content = uiState.value as? ChangePasswordUiState.Content
        if (content?.activePanel == ChangePasswordScreenPanel.ResetPassword) {
            uiStateMachine.setActivePanel(ChangePasswordScreenPanel.ChangePassword)
        } else {
            _navigation.update { ChangePasswordNavigationState.GoBack }
        }
    }

    private fun onChangeInput(input: ChangePasswordInput) {
        uiStateMachine.updateInput(input, input.validate())
    }

    private fun changePassword() {
        val content = uiState.value as? ChangePasswordUiState.Content ?: return
        val input = content.input
        val validationError = input.validate()
        uiStateMachine.updateInput(input, validationError)

        if (validationError.hasError()) return

        val userType = settingsRepository.getUserType() ?: return

        uiStateMachine.showLoading(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            authRepo.changePassword(
                userType = userType,
                oldPassword = input.oldPassword,
                newPassword = input.newPassword,
            )
                .onSuccess {
                    uiStateMachine.showLoading(false)
                    _message.update {
                        ChangePasswordMessageState.Success("Password changed successfully!")
                    }
                }
                .onFailure { error ->
                    uiStateMachine.showLoading(false)
                    val msg = error.message ?: "Failed to change password. Please try again."
                    _message.update { ChangePasswordMessageState.Error(msg) }
                }
        }
    }

    private fun sendPasswordResetLink() {
        val content = uiState.value as? ChangePasswordUiState.Content ?: return
        val email = content.resetEmail.trim()
        if (email.isEmpty()) {
            _message.update {
                ChangePasswordMessageState.Error("Please enter your email address.")
            }
            return
        }

        uiStateMachine.showLoading(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            authRepo.resetPassword(email)
                .onSuccess {
                    uiStateMachine.showLoading(false)
                    uiStateMachine.setActivePanel(ChangePasswordScreenPanel.ChangePassword)
                    _message.update {
                        ChangePasswordMessageState.Success(
                            "Password reset link request has been sent to $email",
                        )
                    }
                }
                .onFailure { error ->
                    uiStateMachine.showLoading(false)
                    _message.update {
                        ChangePasswordMessageState.Error(
                            message = error.message ?: "Failed the reset password. Please retry",
                        )
                    }
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
