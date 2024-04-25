package com.workfort.pstuian.app.ui.contactus

import com.workfort.pstuian.model.ContactUsInput
import com.workfort.pstuian.model.ContactUsInputValidationError
import com.workfort.pstuian.view.service.StateUpdate

sealed interface ContactUsScreenStateUpdate : StateUpdate<ContactUsScreenState> {

    data class ShowLoading(val isLoading: Boolean) : ContactUsScreenStateUpdate {
        override fun invoke(
            oldState: ContactUsScreenState,
        ): ContactUsScreenState = with(oldState) {
            copy(displayState = displayState.copy(isLoading = isLoading))
        }
    }

    data class SetContactUsInput(val input: ContactUsInput) : ContactUsScreenStateUpdate {
        override fun invoke(oldState: ContactUsScreenState): ContactUsScreenState = with(oldState) {
            copy(displayState = displayState.copy(contactUsInput = input))
        }
    }

    data class SetValidationError(
        val validationError: ContactUsInputValidationError,
    ) : ContactUsScreenStateUpdate {
        override fun invoke(oldState: ContactUsScreenState): ContactUsScreenState = with(oldState) {
            copy(displayState = displayState.copy(validationError = validationError))
        }
    }

    data class UpdateMessageState(
        val messageState: ContactUsScreenState.DisplayState.MessageState,
    ) : ContactUsScreenStateUpdate {
        override fun invoke(
            oldState: ContactUsScreenState
        ): ContactUsScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : ContactUsScreenStateUpdate {
        override fun invoke(
            oldState: ContactUsScreenState
        ): ContactUsScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: ContactUsScreenState.NavigationState,
    ) : ContactUsScreenStateUpdate {
        override fun invoke(
            oldState: ContactUsScreenState,
        ): ContactUsScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : ContactUsScreenStateUpdate {
        override fun invoke(
            oldState: ContactUsScreenState,
        ): ContactUsScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}