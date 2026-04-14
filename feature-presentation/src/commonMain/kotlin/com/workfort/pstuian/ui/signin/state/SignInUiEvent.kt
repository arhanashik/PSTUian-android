package com.workfort.pstuian.ui.signin.state

import com.workfort.pstuian.featuredomain.model.UserType

sealed interface SignInUiEvent {
    data object BackClicked : SignInUiEvent
    data class UserTypeBtnClicked(val userType: UserType) : SignInUiEvent
    data class SignInClicked(val email: String, val password: String) : SignInUiEvent
    data object ForgotPasswordClicked : SignInUiEvent
    data object SignUpClicked : SignInUiEvent
    data object EmailVerificationClicked : SignInUiEvent
    data object MessageConsumed : SignInUiEvent
    data object NavigationConsumed : SignInUiEvent
}