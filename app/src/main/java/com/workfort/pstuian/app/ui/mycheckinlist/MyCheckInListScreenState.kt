package com.workfort.pstuian.app.ui.mycheckinlist

import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.model.CheckInPrivacy


data class MyCheckInListScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val isSignedIn: Boolean,
        val checkInListState: CheckInListState,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                isSignedIn = false,
                checkInListState = CheckInListState.None,
                messageState = null,
            )
        }

        sealed interface CheckInListState {
            data object None : CheckInListState
            data class Available(
                val items: List<CheckInEntity>,
                val isLoading: Boolean,
            ) : CheckInListState
            data class Error(val message: String) : CheckInListState
        }

        sealed interface MessageState {
            data class Loading(val cancelable: Boolean) : MessageState
            data class ShowDetails(val item: CheckInEntity) : MessageState
            data class ConfirmPrivacyChange(
                val item: CheckInEntity,
                val privacy: CheckInPrivacy,
            ) : MessageState
            data class ConfirmDelete(val item: CheckInEntity) : MessageState
            data class Success(val message: String) : MessageState
            data class Error(val message: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
    }
}