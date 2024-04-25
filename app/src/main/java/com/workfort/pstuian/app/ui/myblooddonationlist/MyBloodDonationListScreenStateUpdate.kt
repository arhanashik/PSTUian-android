package com.workfort.pstuian.app.ui.myblooddonationlist

import com.workfort.pstuian.model.BloodDonationEntity
import com.workfort.pstuian.view.service.StateUpdate


sealed interface MyBloodDonationListScreenStateUpdate : StateUpdate<MyBloodDonationListScreenState> {

    data class ShowMyBloodDonationList(
        val items: List<BloodDonationEntity>,
        val isLoading: Boolean,
    ) : MyBloodDonationListScreenStateUpdate {
        override fun invoke(
            oldState: MyBloodDonationListScreenState
        ): MyBloodDonationListScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    listState = MyBloodDonationListScreenState.DisplayState.BloodDonationListState
                        .Available(items, isLoading),
                ),
            )
        }
    }

    data class DataLoadFailed(val message: String) : MyBloodDonationListScreenStateUpdate {
        override fun invoke(
            oldState: MyBloodDonationListScreenState
        ): MyBloodDonationListScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    listState = MyBloodDonationListScreenState.DisplayState.BloodDonationListState
                        .Error(message),
                ),
            )
        }
    }

    data class UpdateMessageState(
        val messageState: MyBloodDonationListScreenState.DisplayState.MessageState
    ) : MyBloodDonationListScreenStateUpdate {
        override fun invoke(
            oldState: MyBloodDonationListScreenState
        ): MyBloodDonationListScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : MyBloodDonationListScreenStateUpdate {
        override fun invoke(
            oldState: MyBloodDonationListScreenState
        ): MyBloodDonationListScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: MyBloodDonationListScreenState.NavigationState,
    ) : MyBloodDonationListScreenStateUpdate {
        override fun invoke(
            oldState: MyBloodDonationListScreenState,
        ): MyBloodDonationListScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : MyBloodDonationListScreenStateUpdate {
        override fun invoke(
            oldState: MyBloodDonationListScreenState,
        ): MyBloodDonationListScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}