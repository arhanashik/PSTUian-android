package com.workfort.pstuian.app.ui.myblooddonationlist

import com.workfort.pstuian.model.BloodDonationEntity


data class MyBloodDonationListScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val listState: BloodDonationListState,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                listState = BloodDonationListState.None,
                messageState = null,
            )
        }
        sealed interface BloodDonationListState {
            data object None : BloodDonationListState
            data class Available(
                val items: List<BloodDonationEntity>,
                val isLoading: Boolean,
            ) : BloodDonationListState
            data class Error(val message: String) : BloodDonationListState
        }
        sealed interface MessageState {
            data class ConfirmDelete(val item: BloodDonationEntity) : MessageState
            data class Loading(val cancelable: Boolean) : MessageState
            data class Success(val message: String) : MessageState
            data class Failure(val message: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
        data class BloodDonationRequestEditScreen(val item: BloodDonationEntity) : NavigationState
        data object BloodDonationRequestCreateScreen : NavigationState
    }
}