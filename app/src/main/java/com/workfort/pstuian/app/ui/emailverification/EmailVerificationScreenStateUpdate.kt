package com.workfort.pstuian.app.ui.emailverification

import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.view.service.StateUpdate

sealed interface EmailVerificationScreenStateUpdate : StateUpdate<EmailVerificationScreenState> {

    data class ShowLoading(val isLoading: Boolean) : EmailVerificationScreenStateUpdate {
        override fun invoke(
            oldState: EmailVerificationScreenState,
        ): EmailVerificationScreenState = with(oldState) {
            copy(displayState = displayState.copy(isLoading = isLoading))
        }
    }

    data class ChangeUserType(val userType: UserType) : EmailVerificationScreenStateUpdate {
        override fun invoke(
            oldState: EmailVerificationScreenState,
        ): EmailVerificationScreenState = with(oldState) {
            copy(displayState = displayState.copy(userType = userType))
        }
    }

    data class SetValidationError(val error: String?) : EmailVerificationScreenStateUpdate {
        override fun invoke(
            oldState: EmailVerificationScreenState,
        ): EmailVerificationScreenState = with(oldState) {
            copy(displayState = displayState.copy(validationError = error))
        }
    }

    data class UpdateMessageState(
        val messageState: EmailVerificationScreenState.DisplayState.MessageState,
    ) : EmailVerificationScreenStateUpdate {
        override fun invoke(
            oldState: EmailVerificationScreenState
        ): EmailVerificationScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : EmailVerificationScreenStateUpdate {
        override fun invoke(
            oldState: EmailVerificationScreenState
        ): EmailVerificationScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: EmailVerificationScreenState.NavigationState,
    ) : EmailVerificationScreenStateUpdate {
        override fun invoke(
            oldState: EmailVerificationScreenState,
        ): EmailVerificationScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : EmailVerificationScreenStateUpdate {
        override fun invoke(
            oldState: EmailVerificationScreenState,
        ): EmailVerificationScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}