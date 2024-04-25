package com.workfort.pstuian.app.ui.blooddonationcreate

import com.workfort.pstuian.model.BloodDonationInput
import com.workfort.pstuian.model.BloodDonationInputError


data class BloodDonationCreateScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val isLoading: Boolean,
        val input: BloodDonationInput,
        val validationError: BloodDonationInputError,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                isLoading = false,
                input = BloodDonationInput.INITIAL,
                validationError = BloodDonationInputError.INITIAL,
                messageState = null,
            )
        }
        sealed interface MessageState {
            data object SelectDate : MessageState
            data class SendRequestSuccess(val message: String) : MessageState
            data class Error(val message: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
    }
}