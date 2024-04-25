package com.workfort.pstuian.app.ui.donate

import com.workfort.pstuian.model.DonationInput
import com.workfort.pstuian.model.DonationInputValidationError
import com.workfort.pstuian.view.service.StateUpdate

sealed interface DonateScreenStateUpdate : StateUpdate<DonateScreenState> {

    data class ShowLoading(val isLoading: Boolean) : DonateScreenStateUpdate {
        override fun invoke(
            oldState: DonateScreenState,
        ): DonateScreenState = with(oldState) {
            copy(displayState = displayState.copy(isLoading = isLoading))
        }
    }

    data class SetDonationOption(val option: String) : DonateScreenStateUpdate {
        override fun invoke(oldState: DonateScreenState): DonateScreenState = with(oldState) {
            copy(displayState = displayState.copy(donationOption = option))
        }
    }

    data class SetDonationInput(val input: DonationInput) : DonateScreenStateUpdate {
        override fun invoke(oldState: DonateScreenState): DonateScreenState = with(oldState) {
            copy(displayState = displayState.copy(donationInput = input))
        }
    }

    data class SetValidationError(
        val validationError: DonationInputValidationError,
    ) : DonateScreenStateUpdate {
        override fun invoke(oldState: DonateScreenState): DonateScreenState = with(oldState) {
            copy(displayState = displayState.copy(validationError = validationError))
        }
    }

    data class UpdateMessageState(
        val messageState: DonateScreenState.DisplayState.MessageState,
    ) : DonateScreenStateUpdate {
        override fun invoke(
            oldState: DonateScreenState
        ): DonateScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : DonateScreenStateUpdate {
        override fun invoke(
            oldState: DonateScreenState
        ): DonateScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: DonateScreenState.NavigationState,
    ) : DonateScreenStateUpdate {
        override fun invoke(
            oldState: DonateScreenState,
        ): DonateScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : DonateScreenStateUpdate {
        override fun invoke(
            oldState: DonateScreenState,
        ): DonateScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}