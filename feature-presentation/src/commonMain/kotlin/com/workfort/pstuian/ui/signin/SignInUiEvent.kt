package com.workfort.pstuian.ui.signin

import com.workfort.pstuian.featuredomain.model.UserType

sealed interface SignInUiEvent {
    data object OnClickBack : SignInUiEvent
    data class OnClickUserTypeBtn(val userType: UserType) : SignInUiEvent
    data class OnClickSignIn(val email: String, val password: String) : SignInUiEvent
    data object OnClickForgotPassword : SignInUiEvent
    data object OnClickSignUp : SignInUiEvent
    data object OnClickEmailVerification : SignInUiEvent
    data object MessageConsumed : SignInUiEvent
    data object NavigationConsumed : SignInUiEvent
}
