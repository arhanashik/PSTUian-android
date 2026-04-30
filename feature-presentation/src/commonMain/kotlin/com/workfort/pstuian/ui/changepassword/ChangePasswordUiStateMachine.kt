package com.workfort.pstuian.ui.changepassword

import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInput
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInputError
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiState
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChangePasswordUiStateMachine : UiStateMachine<ChangePasswordUiState> {

    private val _state = MutableStateFlow<ChangePasswordUiState>(ChangePasswordUiState.None)
    override val uiState: StateFlow<ChangePasswordUiState> = _state.asStateFlow()

    private fun updateUiState(
        updater: ChangePasswordUiState.() -> ChangePasswordUiState,
    ) = _state.update(updater)

    fun transitionToResetPasswordForm(oobCode: String) = updateUiState {
        ChangePasswordUiState.ResetPassword(oobCode = oobCode)
    }

    fun transitionToSendResetPasswordLink(prefilledEmail: String?) = updateUiState {
        ChangePasswordUiState.SendResetPasswordLink(email = prefilledEmail.orEmpty())
    }

    fun transitionToChangePasswordForm() = updateUiState {
        ChangePasswordUiState.ChangePassword()
    }

    fun updateChangePasswordInput(input: ChangePasswordInput) = updateUiState {
        when (this) {
            is ChangePasswordUiState.ChangePassword -> copy(input = input)
            else -> this
        }
    }

    fun updateChangePasswordInputError(validationError: ChangePasswordInputError) = updateUiState {
        when (this) {
            is ChangePasswordUiState.ChangePassword -> copy(validationError = validationError)
            else -> this
        }
    }

    fun updateSendLinkEmail(email: String) = updateUiState {
        when (this) {
            is ChangePasswordUiState.SendResetPasswordLink -> copy(email = email, validationError = "")
            else -> this
        }
    }

    fun setSendLinkValidationError(error: String) = updateUiState {
        when (this) {
            is ChangePasswordUiState.SendResetPasswordLink -> copy(validationError = error)
            else -> this
        }
    }

    fun updateOobNewPassword(value: String) = updateUiState {
        when (this) {
            is ChangePasswordUiState.ResetPassword ->
                copy(newPassword = value, newPasswordError = "", confirmPasswordError = "")
            else -> this
        }
    }

    fun updateOobConfirmPassword(value: String) = updateUiState {
        when (this) {
            is ChangePasswordUiState.ResetPassword ->
                copy(confirmPassword = value, confirmPasswordError = "", newPasswordError = "")
            else -> this
        }
    }

    fun setOobPasswordFieldErrors(newPasswordError: String, confirmPasswordError: String) = updateUiState {
        when (this) {
            is ChangePasswordUiState.ResetPassword ->
                copy(newPasswordError = newPasswordError, confirmPasswordError = confirmPasswordError)
            else -> this
        }
    }
}
