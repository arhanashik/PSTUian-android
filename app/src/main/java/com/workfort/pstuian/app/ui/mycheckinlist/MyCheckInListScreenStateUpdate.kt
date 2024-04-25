package com.workfort.pstuian.app.ui.mycheckinlist

import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.view.service.StateUpdate

sealed interface MyCheckInListScreenStateUpdate : StateUpdate<MyCheckInListScreenState> {

    data class UpdateSignedInState(val isSignedIn: Boolean) : MyCheckInListScreenStateUpdate {
        override fun invoke(
            oldState: MyCheckInListScreenState,
        ): MyCheckInListScreenState = with(oldState) {
            copy(displayState = displayState.copy(isSignedIn = isSignedIn))
        }
    }

    data class ShowDataList(
        val items: List<CheckInEntity>,
        val isLoading: Boolean,
    ) : MyCheckInListScreenStateUpdate {
        override fun invoke(
            oldState: MyCheckInListScreenState
        ): MyCheckInListScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    checkInListState = MyCheckInListScreenState.DisplayState.CheckInListState
                        .Available(items = items, isLoading = isLoading),
                ),
            )
        }
    }

    data class DataLoadFailed(val message: String) : MyCheckInListScreenStateUpdate {
        override fun invoke(
            oldState: MyCheckInListScreenState
        ): MyCheckInListScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    checkInListState = MyCheckInListScreenState.DisplayState.CheckInListState
                        .Error(message),
                ),
            )
        }
    }

    data class UpdateMessageState(
        val newState: MyCheckInListScreenState.DisplayState.MessageState
    ) : MyCheckInListScreenStateUpdate {
        override fun invoke(oldState: MyCheckInListScreenState): MyCheckInListScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = newState))
        }
    }

    data object MessageConsumed : MyCheckInListScreenStateUpdate {
        override fun invoke(
            oldState: MyCheckInListScreenState
        ): MyCheckInListScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: MyCheckInListScreenState.NavigationState,
    ) : MyCheckInListScreenStateUpdate {
        override fun invoke(
            oldState: MyCheckInListScreenState,
        ): MyCheckInListScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : MyCheckInListScreenStateUpdate {
        override fun invoke(
            oldState: MyCheckInListScreenState,
        ): MyCheckInListScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}