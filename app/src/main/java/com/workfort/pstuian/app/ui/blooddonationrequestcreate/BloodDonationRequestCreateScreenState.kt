package com.workfort.pstuian.app.ui.blooddonationrequestcreate

import com.workfort.pstuian.model.BloodDonationRequestInput
import com.workfort.pstuian.model.BloodDonationRequestInputError


data class BloodDonationRequestCreateScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val isLoading: Boolean,
        val input: BloodDonationRequestInput,
        val validationError: BloodDonationRequestInputError,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                isLoading = false,
                input = BloodDonationRequestInput.INITIAL,
                validationError = BloodDonationRequestInputError.INITIAL,
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