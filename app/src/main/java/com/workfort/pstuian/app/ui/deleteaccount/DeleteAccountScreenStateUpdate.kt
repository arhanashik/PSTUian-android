package com.workfort.pstuian.app.ui.deleteaccount

import com.workfort.pstuian.view.service.StateUpdate

sealed interface DeleteAccountScreenStateUpdate : StateUpdate<DeleteAccountScreenState> {

    data class UpdateInput(
        val input: String,
        val validationError: String,
    ) : DeleteAccountScreenStateUpdate {
        override fun invoke(
            oldState: DeleteAccountScreenState,
        ): DeleteAccountScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    input = input,
                    validationError = validationError,
                ),
            )
        }
    }

    data class UpdateMessageState(
        val messageState: DeleteAccountScreenState.DisplayState.MessageState,
    ) : DeleteAccountScreenStateUpdate {
        override fun invoke(
            oldState: DeleteAccountScreenState
        ): DeleteAccountScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : DeleteAccountScreenStateUpdate {
        override fun invoke(
            oldState: DeleteAccountScreenState
        ): DeleteAccountScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: DeleteAccountScreenState.NavigationState,
    ) : DeleteAccountScreenStateUpdate {
        override fun invoke(
            oldState: DeleteAccountScreenState,
        ): DeleteAccountScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : DeleteAccountScreenStateUpdate {
        override fun invoke(
            oldState: DeleteAccountScreenState,
        ): DeleteAccountScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}