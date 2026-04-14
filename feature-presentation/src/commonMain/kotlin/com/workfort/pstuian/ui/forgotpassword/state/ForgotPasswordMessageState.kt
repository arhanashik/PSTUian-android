package com.workfort.pstuian.ui.forgotpassword.state

sealed interface ForgotPasswordMessageState {
    data class PasswordResetLinkSentSuccess(val message: String) : ForgotPasswordMessageState
    data class Error(val message: String) : ForgotPasswordMessageState
}
