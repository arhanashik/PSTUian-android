package com.workfort.pstuian.app.ui.emailverification

import com.workfort.pstuian.model.UserType

sealed class EmailVerificationScreenUiEvent {
    data object None : EmailVerificationScreenUiEvent()
    data object OnClickBack : EmailVerificationScreenUiEvent()
    data class OnClickUserTypeBtn(val userType: UserType) : EmailVerificationScreenUiEvent()
    data object OnClickSignIn : EmailVerificationScreenUiEvent()
    data class OnClickSendEmail(val email: String) : EmailVerificationScreenUiEvent()
    data object MessageConsumed : EmailVerificationScreenUiEvent()
    data object NavigationConsumed : EmailVerificationScreenUiEvent()
}