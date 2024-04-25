package com.workfort.pstuian.app.ui.blooddonationrequestcreate

import com.workfort.pstuian.model.BloodDonationRequestInput
import com.workfort.pstuian.model.BloodDonationRequestInputError
import com.workfort.pstuian.view.service.StateUpdate

sealed interface BloodDonationRequestCreateScreenStateUpdate : StateUpdate<BloodDonationRequestCreateScreenState> {

    data class ShowLoading(val isLoading: Boolean) : BloodDonationRequestCreateScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationRequestCreateScreenState,
        ): BloodDonationRequestCreateScreenState = with(oldState) {
            copy(displayState = displayState.copy(isLoading = isLoading))
        }
    }

    data class SetInput(
        val input: BloodDonationRequestInput,
    ) : BloodDonationRequestCreateScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationRequestCreateScreenState,
        ): BloodDonationRequestCreateScreenState = with(oldState) {
            copy(displayState = displayState.copy(input = input))
        }
    }

    data class SetValidationError(
        val validationError: BloodDonationRequestInputError,
    ) : BloodDonationRequestCreateScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationRequestCreateScreenState,
        ): BloodDonationRequestCreateScreenState = with(oldState) {
            copy(displayState = displayState.copy(validationError = validationError))
        }
    }

    data class UpdateMessageState(
        val messageState: BloodDonationRequestCreateScreenState.DisplayState.MessageState,
    ) : BloodDonationRequestCreateScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationRequestCreateScreenState
        ): BloodDonationRequestCreateScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : BloodDonationRequestCreateScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationRequestCreateScreenState
        ): BloodDonationRequestCreateScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: BloodDonationRequestCreateScreenState.NavigationState,
    ) : BloodDonationRequestCreateScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationRequestCreateScreenState,
        ): BloodDonationRequestCreateScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : BloodDonationRequestCreateScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationRequestCreateScreenState,
        ): BloodDonationRequestCreateScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}