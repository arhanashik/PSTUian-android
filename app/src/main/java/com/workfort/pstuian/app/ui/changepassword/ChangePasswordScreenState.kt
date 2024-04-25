package com.workfort.pstuian.app.ui.changepassword

import com.workfort.pstuian.model.ChangePasswordInput
import com.workfort.pstuian.model.ChangePasswordInputError


data class ChangePasswordScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val input: ChangePasswordInput,
        val validationError: ChangePasswordInputError,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                input = ChangePasswordInput.INITIAL,
                validationError = ChangePasswordInputError.INITIAL,
                messageState = null,
            )
        }
        sealed interface MessageState {
            data class Loading(val cancelable: Boolean) : MessageState
            data class Success(val message: String) : MessageState
            data class Error(val message: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
    }
}