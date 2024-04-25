package com.workfort.pstuian.app.ui.changepassword

import com.workfort.pstuian.model.ChangePasswordInput
import com.workfort.pstuian.model.ChangePasswordInputError
import com.workfort.pstuian.view.service.StateUpdate

sealed interface ChangePasswordScreenStateUpdate : StateUpdate<ChangePasswordScreenState> {

    data class UpdateInput(
        val input: ChangePasswordInput,
        val validationError: ChangePasswordInputError,
    ) : ChangePasswordScreenStateUpdate {
        override fun invoke(
            oldState: ChangePasswordScreenState,
        ): ChangePasswordScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    input = input,
                    validationError = validationError,
                ),
            )
        }
    }

    data class UpdateMessageState(
        val messageState: ChangePasswordScreenState.DisplayState.MessageState,
    ) : ChangePasswordScreenStateUpdate {
        override fun invoke(
            oldState: ChangePasswordScreenState
        ): ChangePasswordScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : ChangePasswordScreenStateUpdate {
        override fun invoke(
            oldState: ChangePasswordScreenState
        ): ChangePasswordScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: ChangePasswordScreenState.NavigationState,
    ) : ChangePasswordScreenStateUpdate {
        override fun invoke(
            oldState: ChangePasswordScreenState,
        ): ChangePasswordScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : ChangePasswordScreenStateUpdate {
        override fun invoke(
            oldState: ChangePasswordScreenState,
        ): ChangePasswordScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}