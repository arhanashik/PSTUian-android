package com.workfort.pstuian.app.ui.forgotpassword

import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.view.service.StateUpdate

sealed interface ForgotPasswordScreenStateUpdate : StateUpdate<ForgotPasswordScreenState> {

    data class ShowLoading(val isLoading: Boolean) : ForgotPasswordScreenStateUpdate {
        override fun invoke(
            oldState: ForgotPasswordScreenState,
        ): ForgotPasswordScreenState = with(oldState) {
            copy(displayState = displayState.copy(isLoading = isLoading))
        }
    }

    data class ChangeUserType(val userType: UserType) : ForgotPasswordScreenStateUpdate {
        override fun invoke(
            oldState: ForgotPasswordScreenState,
        ): ForgotPasswordScreenState = with(oldState) {
            copy(displayState = displayState.copy(userType = userType))
        }
    }

    data class SetValidationError(val error: String?) : ForgotPasswordScreenStateUpdate {
        override fun invoke(
            oldState: ForgotPasswordScreenState,
        ): ForgotPasswordScreenState = with(oldState) {
            copy(displayState = displayState.copy(validationError = error))
        }
    }

    data class UpdateMessageState(
        val messageState: ForgotPasswordScreenState.DisplayState.MessageState,
    ) : ForgotPasswordScreenStateUpdate {
        override fun invoke(
            oldState: ForgotPasswordScreenState
        ): ForgotPasswordScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : ForgotPasswordScreenStateUpdate {
        override fun invoke(
            oldState: ForgotPasswordScreenState
        ): ForgotPasswordScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: ForgotPasswordScreenState.NavigationState,
    ) : ForgotPasswordScreenStateUpdate {
        override fun invoke(
            oldState: ForgotPasswordScreenState,
        ): ForgotPasswordScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : ForgotPasswordScreenStateUpdate {
        override fun invoke(
            oldState: ForgotPasswordScreenState,
        ): ForgotPasswordScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}