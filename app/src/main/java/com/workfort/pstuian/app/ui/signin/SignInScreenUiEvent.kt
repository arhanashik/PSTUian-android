package com.workfort.pstuian.app.ui.signin

import com.workfort.pstuian.model.UserType

sealed class SignInScreenUiEvent {
    data object None : SignInScreenUiEvent()
    data object OnClickBack : SignInScreenUiEvent()
    data class OnClickUserTypeBtn(val userType: UserType) : SignInScreenUiEvent()
    data class OnClickSignIn(val email: String, val password: String) : SignInScreenUiEvent()
    data object OnClickForgotPassword : SignInScreenUiEvent()
    data object OnClickSignUp : SignInScreenUiEvent()
    data object OnClickEmailVerification : SignInScreenUiEvent()
    data object MessageConsumed : SignInScreenUiEvent()
    data object NavigationConsumed : SignInScreenUiEvent()
}