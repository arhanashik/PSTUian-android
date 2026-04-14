package com.workfort.pstuian.ui.signin.state

sealed interface NavigationState {
    data class GoBack(val isSignedIn: Boolean) : NavigationState
    data object GoToForgotPasswordScreen : NavigationState
    data object GoToSignUpScreen : NavigationState
    data object GoToEmailVerificationScreen : NavigationState
}
