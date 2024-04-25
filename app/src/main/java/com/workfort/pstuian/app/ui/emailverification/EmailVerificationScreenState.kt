package com.workfort.pstuian.app.ui.emailverification

import com.workfort.pstuian.model.UserType


data class EmailVerificationScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val isLoading: Boolean,
        val userType: UserType?,
        val validationError: String?,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                isLoading = false,
                userType = null,
                validationError = null,
                messageState = null,
            )
        }
        sealed interface MessageState {
            data class EmailSentSuccess(val message: String) : MessageState
            data class Error(val message: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
    }
}