package com.workfort.pstuian.ui.changepassword

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.util.deeplink.ResetPasswordParams
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInput
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInputError
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
    private val resetPasswordParams: ResetPasswordParams?,
) : UiStateMachineViewModel<ChangePasswordUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<ChangePasswordMessageState?>(null)
    val message: StateFlow<ChangePasswordMessageState?> = _message

    private val _navigation = MutableStateFlow<ChangePasswordNavigationState?>(null)
    val navigation: StateFlow<ChangePasswordNavigationState?> = _navigation

    override fun onUiReady() {
        val prefilled = screenData.getCurrentUser()?.email?.trim().orEmpty()
        val oobCode = resetPasswordParams?.oobCode?.trim().takeUnless { it.isNullOrEmpty() }
        when {
            oobCode != null -> uiStateMachine.transitionToResetPasswordForm(oobCode = oobCode)
            resetPasswordParams == null || !resetPasswordParams.hasAnyValue() ->
                uiStateMachine.transitionToChangePasswordForm()
            else ->
                uiStateMachine.transitionToSendResetPasswordLink(prefilledEmail = prefilled)
        }
    }

    fun onUiEvent(event: ChangePasswordUiEvent) {
        when (event) {
            is ChangePasswordUiEvent.BackClicked -> onBack()
            is ChangePasswordUiEvent.ChangePasswordInputChanged -> uiStateMachine.updateChangePasswordInput(event.input)
            is ChangePasswordUiEvent.ChangePasswordClicked -> onClickChangePassword(event.input)
            is ChangePasswordUiEvent.OpenSendResetPasswordLinkPanel ->
                uiStateMachine.transitionToSendResetPasswordLink(screenData.getCurrentUser()?.email?.trim())
            is ChangePasswordUiEvent.SwitchToChangePasswordPanel -> uiStateMachine.transitionToChangePasswordForm()
            is ChangePasswordUiEvent.SendResetLinkEmailChanged -> uiStateMachine.updateSendLinkEmail(event.email)
            is ChangePasswordUiEvent.SendPasswordResetLinkClicked -> onSendResetLink()
            is ChangePasswordUiEvent.OobNewPasswordChanged -> uiStateMachine.updateOobNewPassword(event.value)
            is ChangePasswordUiEvent.OobConfirmPasswordChanged -> uiStateMachine.updateOobConfirmPassword(event.value)
            is ChangePasswordUiEvent.OobSubmitNewPasswordClicked -> onSubmitOobNewPassword()
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
                uiStateMachine.transitionToChangePasswordForm()
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

    private fun onSendResetLink() {
        val state = uiState.value as? ChangePasswordUiState.SendResetPasswordLink ?: return
        val email = state.email.trim()
        if (email.isEmpty()) {
            uiStateMachine.setSendLinkValidationError("*Required")
            return
        }
        _message.update { ChangePasswordMessageState.Loader() }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            authRepository.resetPassword(email)
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

    private fun onSubmitOobNewPassword() {
        val state = uiState.value as? ChangePasswordUiState.ResetPassword ?: return
        var newErr = ""
        var confirmErr = ""
        when {
            state.newPassword.isEmpty() -> newErr = "*Required"
            state.newPassword.length < 6 -> newErr = "*Too short"
        }
        when {
            state.confirmPassword.isEmpty() -> confirmErr = "*Required"
            state.confirmPassword.length < 6 -> confirmErr = "*Too short"
            state.newPassword.isNotEmpty() && state.newPassword != state.confirmPassword ->
                confirmErr = "*Confirm password should match new password"
        }
        if (newErr.isNotEmpty() || confirmErr.isNotEmpty()) {
            uiStateMachine.setOobPasswordFieldErrors(newErr, confirmErr)
            return
        }
        _message.update { ChangePasswordMessageState.Loader() }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            authRepository.confirmPasswordReset(state.oobCode, state.newPassword)
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
                        ChangePasswordMessageState.Error(error.message ?: "Could not reset password. Please retry.")
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
