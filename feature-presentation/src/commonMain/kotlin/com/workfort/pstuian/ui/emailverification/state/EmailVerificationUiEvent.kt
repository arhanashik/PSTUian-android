package com.workfort.pstuian.ui.emailverification.state

import com.workfort.pstuian.featuredomain.model.UserType

sealed interface EmailVerificationUiEvent {
    data object OnClickBack : EmailVerificationUiEvent
    data class OnClickUserTypeBtn(val userType: UserType) : EmailVerificationUiEvent
    data object OnClickSignIn : EmailVerificationUiEvent
    data class OnClickSendEmail(val email: String) : EmailVerificationUiEvent
    data object MessageConsumed : EmailVerificationUiEvent
    data object NavigationConsumed : EmailVerificationUiEvent
}
