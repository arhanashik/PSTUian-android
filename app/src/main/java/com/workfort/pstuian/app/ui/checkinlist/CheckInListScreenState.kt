package com.workfort.pstuian.app.ui.checkinlist

import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.model.CheckInLocationEntity
import com.workfort.pstuian.model.UserType


data class CheckInListScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val checkInLocation: CheckInLocationEntity?,
        val checkInListState: CheckInListState,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                checkInLocation = null,
                checkInListState = CheckInListState.None,
                messageState = null,
            )
        }
        sealed interface CheckInListState {
            data object None : CheckInListState
            data class Available(
                val checkInList: List<CheckInEntity>,
                val isLoading: Boolean,
            ) : CheckInListState
            data class Error(val message: String) : CheckInListState
        }
        sealed interface MessageState {
            data class Loading(val cancelable: Boolean) : MessageState
            data class Call(val phoneNumber: String) : MessageState
            data class ConfirmCheckIn(val location: CheckInLocationEntity) : MessageState
            data class Success(val message: String) : MessageState
            data class Error(val message: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
        data object LocationPickerScreen : NavigationState
        data class ProfileScreen(
            val userId: Int,
            val userType: UserType,
        ) : NavigationState
    }
}