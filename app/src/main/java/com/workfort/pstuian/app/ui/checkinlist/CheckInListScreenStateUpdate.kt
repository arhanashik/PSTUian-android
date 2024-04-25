package com.workfort.pstuian.app.ui.checkinlist

import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.model.CheckInLocationEntity
import com.workfort.pstuian.view.service.StateUpdate


sealed interface CheckInListScreenStateUpdate : StateUpdate<CheckInListScreenState> {

    data class ShowCheckInListLocation(
        val checkInLocation: CheckInLocationEntity?,
    ) : CheckInListScreenStateUpdate {
        override fun invoke(
            oldState: CheckInListScreenState
        ): CheckInListScreenState = with(oldState) {
            copy(displayState = displayState.copy(checkInLocation = checkInLocation))
        }
    }

    data class ShowCheckInListList(
        val checkInList: List<CheckInEntity>,
        val isLoading: Boolean,
    ) : CheckInListScreenStateUpdate {
        override fun invoke(
            oldState: CheckInListScreenState
        ): CheckInListScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    checkInListState = CheckInListScreenState.DisplayState.CheckInListState
                        .Available(checkInList, isLoading)
                )
            )
        }
    }

    data class CheckInListListLoadFailed(val message: String) : CheckInListScreenStateUpdate {
        override fun invoke(
            oldState: CheckInListScreenState
        ): CheckInListScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    checkInListState = CheckInListScreenState.DisplayState.CheckInListState
                        .Error(message)
                )
            )
        }
    }

    data class UpdateMessageState(
        val messageState: CheckInListScreenState.DisplayState.MessageState
    ) : CheckInListScreenStateUpdate {
        override fun invoke(
            oldState: CheckInListScreenState
        ): CheckInListScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : CheckInListScreenStateUpdate {
        override fun invoke(
            oldState: CheckInListScreenState
        ): CheckInListScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: CheckInListScreenState.NavigationState,
    ) : CheckInListScreenStateUpdate {
        override fun invoke(
            oldState: CheckInListScreenState,
        ): CheckInListScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : CheckInListScreenStateUpdate {
        override fun invoke(
            oldState: CheckInListScreenState,
        ): CheckInListScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}