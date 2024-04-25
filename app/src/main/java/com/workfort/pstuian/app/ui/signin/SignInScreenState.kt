package com.workfort.pstuian.app.ui.signin

import com.workfort.pstuian.model.UserType


data class SignInScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val isLoading: Boolean,
        val userType: UserType?,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                isLoading = false,
                userType = null,
                messageState = null,
            )
        }
        sealed interface MessageState {
            data class Success(val message: String, val showToast: Boolean) : MessageState
            data class Error(val message: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data class GoBack(val isSignedIn: Boolean) : NavigationState
        data object GoToForgotPasswordScreen : NavigationState
        data object GoToSignUpScreen : NavigationState
        data object GoToEmailVerificationScreen : NavigationState
    }
}