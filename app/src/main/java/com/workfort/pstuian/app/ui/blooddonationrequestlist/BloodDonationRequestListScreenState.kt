package com.workfort.pstuian.app.ui.blooddonationrequestlist

import com.workfort.pstuian.model.BloodDonationRequestEntity


data class BloodDonationRequestListScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val requestListState: BloodDonationRequestListState,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                requestListState = BloodDonationRequestListState.None,
                messageState = null,
            )
        }
        sealed interface BloodDonationRequestListState {
            data object None : BloodDonationRequestListState
            data class Available(
                val requestList: List<BloodDonationRequestEntity>,
                val isLoading: Boolean,
            ) : BloodDonationRequestListState
            data class Error(val message: String) : BloodDonationRequestListState
        }
        sealed interface MessageState {
            data class ShowDetails(val item: BloodDonationRequestEntity) : MessageState
            data class Call(val phoneNumber: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
        data object BloodDonationRequestCreateScreen : NavigationState
    }
}