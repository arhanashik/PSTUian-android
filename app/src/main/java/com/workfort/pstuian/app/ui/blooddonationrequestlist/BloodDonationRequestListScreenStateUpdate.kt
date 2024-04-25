package com.workfort.pstuian.app.ui.blooddonationrequestlist

import com.workfort.pstuian.model.BloodDonationRequestEntity
import com.workfort.pstuian.view.service.StateUpdate


sealed interface BloodDonationRequestListScreenStateUpdate : StateUpdate<BloodDonationRequestListScreenState> {

    data class ShowBloodDonationRequestList(
        val requestList: List<BloodDonationRequestEntity>,
        val isLoading: Boolean,
    ) : BloodDonationRequestListScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationRequestListScreenState
        ): BloodDonationRequestListScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    requestListState = BloodDonationRequestListScreenState.DisplayState
                        .BloodDonationRequestListState.Available(requestList, isLoading)
                )
            )
        }
    }

    data class DataLoadFailed(val message: String) : BloodDonationRequestListScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationRequestListScreenState
        ): BloodDonationRequestListScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    requestListState = BloodDonationRequestListScreenState.DisplayState
                        .BloodDonationRequestListState.Error(message)
                )
            )
        }
    }

    data class UpdateMessageState(
        val messageState: BloodDonationRequestListScreenState.DisplayState.MessageState
    ) : BloodDonationRequestListScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationRequestListScreenState
        ): BloodDonationRequestListScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : BloodDonationRequestListScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationRequestListScreenState
        ): BloodDonationRequestListScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: BloodDonationRequestListScreenState.NavigationState,
    ) : BloodDonationRequestListScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationRequestListScreenState,
        ): BloodDonationRequestListScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : BloodDonationRequestListScreenStateUpdate {
        override fun invoke(
            oldState: BloodDonationRequestListScreenState,
        ): BloodDonationRequestListScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}