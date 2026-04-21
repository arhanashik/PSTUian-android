package com.workfort.pstuian.ui.signin.state

import com.workfort.pstuian.featuredomain.model.UserType

sealed interface SignInUiEvent {
    data object BackClicked : SignInUiEvent
    data class UserTypeBtnClicked(val userType: UserType) : SignInUiEvent
    data class SignInClicked(val email: String, val password: String) : SignInUiEvent
    data class SignUpSubmitted(val data: SignUpFormData) : SignInUiEvent
    data class ForgotPasswordSubmitted(val email: String) : SignInUiEvent
    data class EmailVerificationSubmitted(val email: String) : SignInUiEvent
    data object MessageConsumed : SignInUiEvent
    data object NavigationConsumed : SignInUiEvent
}
