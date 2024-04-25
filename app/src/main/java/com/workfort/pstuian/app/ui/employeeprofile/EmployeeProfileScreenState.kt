package com.workfort.pstuian.app.ui.employeeprofile

import com.workfort.pstuian.model.EmployeeProfile


data class EmployeeProfileScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val selectedTabIndex: Int,
        val profileState: ProfileState,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                selectedTabIndex = 0,
                profileState = ProfileState.None,
                messageState = null,
            )
        }
        sealed interface ProfileState {
            data object None : ProfileState
            data object Loading : ProfileState
            data class Available(val profile: EmployeeProfile) : ProfileState
            data class Error(val message: String) : ProfileState
        }
        sealed interface MessageState {
            data class Call(val phoneNumber: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
        data class ImagePreviewScreen(val encodedImageUrl: String) : NavigationState
    }
}