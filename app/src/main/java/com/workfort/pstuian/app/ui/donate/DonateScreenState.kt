package com.workfort.pstuian.app.ui.donate

import com.workfort.pstuian.model.DonationInput
import com.workfort.pstuian.model.DonationInputValidationError


data class DonateScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val isLoading: Boolean,
        val donationOption: String,
        val donationInput: DonationInput,
        val validationError: DonationInputValidationError,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                isLoading = false,
                donationOption = "",
                donationInput = DonationInput.INITIAL,
                validationError = DonationInputValidationError.INITIAL,
                messageState = null,
            )
        }
        sealed interface MessageState {
            data class SendDonationSuccess(val message: String) : MessageState
            data class Error(val message: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
    }
}