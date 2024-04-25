package com.workfort.pstuian.app.ui.blooddonationcreate

import com.workfort.pstuian.model.BloodDonationInput
import com.workfort.pstuian.model.BloodDonationInputError
import com.workfort.pstuian.view.service.StateUpdate

sealed interface BloodDonationCreateScreenStateUpdate : StateUpdate<BloodDonationCreateScreenState> {

    data class ShowLoading(val isLoading: Boolean) : BloodDonationCreateScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationCreateScreenState,
        ): BloodDonationCreateScreenState = with(oldState) {
            copy(displayState = displayState.copy(isLoading = isLoading))
        }
    }

    data class SetInput(val input: BloodDonationInput) : BloodDonationCreateScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationCreateScreenState,
        ): BloodDonationCreateScreenState = with(oldState) {
            copy(displayState = displayState.copy(input = input))
        }
    }

    data class SetValidationError(
        val validationError: BloodDonationInputError,
    ) : BloodDonationCreateScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationCreateScreenState,
        ): BloodDonationCreateScreenState = with(oldState) {
            copy(displayState = displayState.copy(validationError = validationError))
        }
    }

    data class UpdateMessageState(
        val messageState: BloodDonationCreateScreenState.DisplayState.MessageState,
    ) : BloodDonationCreateScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationCreateScreenState
        ): BloodDonationCreateScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : BloodDonationCreateScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationCreateScreenState
        ): BloodDonationCreateScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: BloodDonationCreateScreenState.NavigationState,
    ) : BloodDonationCreateScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationCreateScreenState,
        ): BloodDonationCreateScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : BloodDonationCreateScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationCreateScreenState,
        ): BloodDonationCreateScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}