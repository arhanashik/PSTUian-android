package com.workfort.pstuian.ui.changepassword

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInput
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInputError
import com.workfort.pstuian.ui.changepassword.screendata.ResetPasswordInput
import com.workfort.pstuian.ui.changepassword.screendata.ResetPasswordInputError
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordMessageState
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordNavigationState
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiEvent
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiState
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.util.deeplink.ResetPasswordParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ChangePasswordViewModel(
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository,
    private val screenData: SharedScreenData,
    private val uiStateMachine: ChangePasswordUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val resetPasswordParams: ResetPasswordParams?,
) : UiStateMachineViewModel<ChangePasswordUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<ChangePasswordMessageState?>(null)
    val message: StateFlow<ChangePasswordMessageState?> = _message

    private val _navigation = MutableStateFlow<ChangePasswordNavigationState?>(null)
    val navigation: StateFlow<ChangePasswordNavigationState?> = _navigation

    val prefilledEmail = screenData.getCurrentUser()?.email?.trim().orEmpty()

    override fun onUiReady() {
        val oobCode = resetPasswordParams?.oobCode?.trim().takeUnless { it.isNullOrEmpty() }
        when {
            oobCode != null -> uiStateMachine.transitToResetPasswordForm()
            resetPasswordParams == null || !resetPasswordParams.hasAnyValue() ->
                uiStateMachine.transitToChangePasswordForm()
            else ->
                uiStateMachine.transitToSendResetPasswordLink(prefilledEmail = prefilledEmail)
        }
    }

    fun onUiEvent(event: ChangePasswordUiEvent) {
        when (event) {
            is ChangePasswordUiEvent.BackClicked -> onBack()
            is ChangePasswordUiEvent.ChangePasswordInputChanged -> uiStateMachine.updateChangePasswordInput(event.input)
            is ChangePasswordUiEvent.ChangePasswordClicked -> onClickChangePassword(event.input)
            is ChangePasswordUiEvent.OpenSendResetPasswordLinkPanel ->
                uiStateMachine.transitToSendResetPasswordLink(prefilledEmail)
            is ChangePasswordUiEvent.SwitchToChangePasswordPanel -> uiStateMachine.transitToChangePasswordForm()
            is ChangePasswordUiEvent.SendResetLinkEmailChanged -> uiStateMachine.updateSendLinkEmail(event.email)
            is ChangePasswordUiEvent.SendPasswordResetLinkClicked -> onClickSendResetLink(event.email)
            is ChangePasswordUiEvent.ResetPasswordInputChanged -> uiStateMachine.updateResetPasswordInput(event.input)
            is ChangePasswordUiEvent.ResetPasswordClicked -> onClickResetPassword(event.input)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    fun onPostSuccessNavigateToSignIn() {
        _navigation.update { ChangePasswordNavigationState.SignIn }
    }

    private fun onBack() {
        when (uiState.value) {
            is ChangePasswordUiState.SendResetPasswordLink ->
                uiStateMachine.transitToChangePasswordForm()
            else ->
                _navigation.update { ChangePasswordNavigationState.GoBack }
        }
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

    private fun onClickSendResetLink(emailInput: String) {
        val email = emailInput.trim()
        if (emailInput.trim().isEmpty()) {
            uiStateMachine.setSendLinkValidationError("*Required")
            return
        }

        if (email != screenData.getCurrentUser()?.email) {
            _message.update { ChangePasswordMessageState.Error("Please enter the valid email for your account") }
            return
        }

        _message.update { ChangePasswordMessageState.Loader() }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            authRepository.sendResetPasswordLink(email)
                .onSuccess {
                    onMessageHandled()
                    _message.update {
                        ChangePasswordMessageState.Success(
                            message = "Password reset link request has been sent to $email",
                            navigateToSignInAfterDismiss = false,
                        )
                    }
                }
                .onFailure { error ->
                    onMessageHandled()
                    _message.update {
                        ChangePasswordMessageState.Error(error.message ?: "Failed to send reset link. Please retry.")
                    }
                }
        }
    }

    private fun onClickResetPassword(input: ResetPasswordInput) {
        val validationError = input.validate()
        uiStateMachine.setResetPasswordInputError(validationError)

        if (validationError.hasError()) return

        val oobCode = resetPasswordParams?.oobCode ?: return

        _message.update { ChangePasswordMessageState.Loader() }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            authRepository.resetPasswordReset(oobCode, input.newPassword)
                .onSuccess {
                    onMessageHandled()
                    _message.update {
                        ChangePasswordMessageState.Success(
                            message = "Your password has been updated. You can sign in now.",
                            navigateToSignInAfterDismiss = true,
                        )
                    }
                }
                .onFailure { error ->
                    onMessageHandled()
                    _message.update {
                        ChangePasswordMessageState.Error(error.message ?: "Could not reset password. Please retry")
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

    private fun ResetPasswordInput.validate() = ResetPasswordInputError(
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
