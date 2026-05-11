package com.workfort.pstuian.ui.changepassword.state

import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInput
import com.workfort.pstuian.ui.changepassword.screendata.ResetPasswordInput

sealed class ChangePasswordUiEvent {
    data object BackClicked : ChangePasswordUiEvent()
    data class ChangePasswordInputChanged(val input: ChangePasswordInput) : ChangePasswordUiEvent()
    data class ChangePasswordClicked(val input: ChangePasswordInput) : ChangePasswordUiEvent()
    data object OpenSendResetPasswordLinkPanel : ChangePasswordUiEvent()
    data object SwitchToChangePasswordPanel : ChangePasswordUiEvent()
    data class SendResetLinkEmailChanged(val email: String) : ChangePasswordUiEvent()
    data class SendPasswordResetLinkClicked(val email: String) : ChangePasswordUiEvent()
    data class ResetPasswordInputChanged(val input: ResetPasswordInput) : ChangePasswordUiEvent()
    data class ResetPasswordClicked(val input: ResetPasswordInput) : ChangePasswordUiEvent()
}
