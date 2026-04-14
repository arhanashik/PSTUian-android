package com.workfort.pstuian.ui.forgotpassword.state

import com.workfort.pstuian.featuredomain.model.UserType

sealed class ForgotPasswordUiEvent {
    data object BackClicked : ForgotPasswordUiEvent()
    data class UserTypeBtnClicked(val userType: UserType) : ForgotPasswordUiEvent()
    data object SignInClicked : ForgotPasswordUiEvent()
    data class SendResetLinkClicked(val email: String) : ForgotPasswordUiEvent()
}
