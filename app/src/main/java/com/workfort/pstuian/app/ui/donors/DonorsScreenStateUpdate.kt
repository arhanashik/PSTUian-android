package com.workfort.pstuian.app.ui.donors

import com.workfort.pstuian.model.DonorEntity
import com.workfort.pstuian.view.service.StateUpdate


sealed interface DonorsScreenStateUpdate : StateUpdate<DonorsScreenState> {

    data class ShowDonorList(
        val donorEntityList: List<DonorEntity>,
        val isLoading: Boolean,
    ) : DonorsScreenStateUpdate {
        override fun invoke(
            oldState: DonorsScreenState
        ): DonorsScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    donorListState = DonorsScreenState.DisplayState.DonorListState
                        .Available(donorEntityList, isLoading),
                )
            )
        }
    }

    data class DataLoadFailed(val message: String) : DonorsScreenStateUpdate {
        override fun invoke(
            oldState: DonorsScreenState
        ): DonorsScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    donorListState = DonorsScreenState.DisplayState.DonorListState.Error(message),
                )
            )
        }
    }

    data class UpdateMessageState(
        val messageState: DonorsScreenState.DisplayState.MessageState
    ) : DonorsScreenStateUpdate {
        override fun invoke(
            oldState: DonorsScreenState
        ): DonorsScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : DonorsScreenStateUpdate {
        override fun invoke(
            oldState: DonorsScreenState
        ): DonorsScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: DonorsScreenState.NavigationState,
    ) : DonorsScreenStateUpdate {
        override fun invoke(
            oldState: DonorsScreenState,
        ): DonorsScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : DonorsScreenStateUpdate {
        override fun invoke(
            oldState: DonorsScreenState,
        ): DonorsScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}