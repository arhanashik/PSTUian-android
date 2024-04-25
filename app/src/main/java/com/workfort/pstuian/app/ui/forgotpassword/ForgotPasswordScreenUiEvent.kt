package com.workfort.pstuian.app.ui.forgotpassword

import com.workfort.pstuian.model.UserType

sealed class ForgotPasswordScreenUiEvent {
    data object None : ForgotPasswordScreenUiEvent()
    data object OnClickBack : ForgotPasswordScreenUiEvent()
    data class OnClickUserTypeBtn(val userType: UserType) : ForgotPasswordScreenUiEvent()
    data object OnClickSignIn : ForgotPasswordScreenUiEvent()
    data class OnClickSendResetLink(val email: String) : ForgotPasswordScreenUiEvent()
    data object MessageConsumed : ForgotPasswordScreenUiEvent()
    data object NavigationConsumed : ForgotPasswordScreenUiEvent()
}