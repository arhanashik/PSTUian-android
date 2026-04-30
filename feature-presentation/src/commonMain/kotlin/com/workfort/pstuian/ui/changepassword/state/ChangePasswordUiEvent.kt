package com.workfort.pstuian.ui.changepassword.state

import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInput

sealed class ChangePasswordUiEvent {
    data object BackClicked : ChangePasswordUiEvent()
    data class ChangePasswordInputChanged(val input: ChangePasswordInput) : ChangePasswordUiEvent()
    data class ChangePasswordClicked(val input: ChangePasswordInput) : ChangePasswordUiEvent()
    data object OpenSendResetPasswordLinkPanel : ChangePasswordUiEvent()
    data object SwitchToChangePasswordPanel : ChangePasswordUiEvent()
    data class SendResetLinkEmailChanged(val email: String) : ChangePasswordUiEvent()
    data object SendPasswordResetLinkClicked : ChangePasswordUiEvent()
    data class OobNewPasswordChanged(val value: String) : ChangePasswordUiEvent()
    data class OobConfirmPasswordChanged(val value: String) : ChangePasswordUiEvent()
    data object OobSubmitNewPasswordClicked : ChangePasswordUiEvent()
}
