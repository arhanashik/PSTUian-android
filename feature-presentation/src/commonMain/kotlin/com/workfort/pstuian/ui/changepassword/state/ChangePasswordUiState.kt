package com.workfort.pstuian.ui.changepassword.state

import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInput
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInputError

sealed interface ChangePasswordUiState {
    object None : ChangePasswordUiState

    data class ChangePassword(
        val input: ChangePasswordInput = ChangePasswordInput.INITIAL,
        val validationError: ChangePasswordInputError = ChangePasswordInputError.INITIAL,
    ) : ChangePasswordUiState

    data class SendResetPasswordLink(
        val email: String = "",
        val validationError: String = "",
    ) : ChangePasswordUiState

    data class ResetPassword(
        val oobCode: String,
        val newPassword: String = "",
        val confirmPassword: String = "",
        val newPasswordError: String = "",
        val confirmPasswordError: String = "",
    ) : ChangePasswordUiState
}
