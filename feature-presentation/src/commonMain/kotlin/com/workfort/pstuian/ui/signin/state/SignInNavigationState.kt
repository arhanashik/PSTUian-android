package com.workfort.pstuian.ui.signin.state

sealed interface SignInNavigationState {
    data class GoBack(val isSignedIn: Boolean) : SignInNavigationState
    data object GoToEmailVerificationScreen : SignInNavigationState
}
