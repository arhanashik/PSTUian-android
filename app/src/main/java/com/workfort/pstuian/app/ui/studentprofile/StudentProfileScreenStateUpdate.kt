package com.workfort.pstuian.app.ui.studentprofile

import com.workfort.pstuian.model.StudentProfile
import com.workfort.pstuian.view.service.StateUpdate

sealed interface StudentProfileScreenStateUpdate : StateUpdate<StudentProfileScreenState> {

    data class UpdateSelectedTab(val index: Int) : StudentProfileScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileScreenState,
        ): StudentProfileScreenState = with(oldState) {
            copy(displayState = displayState.copy(selectedTabIndex = index))
        }
    }

    data class UpdateSignedInState(val isSignedIn: Boolean) : StudentProfileScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileScreenState,
        ): StudentProfileScreenState = with(oldState) {
            copy(displayState = displayState.copy(isSignedIn = isSignedIn))
        }
    }

    data object ProfileLoading : StudentProfileScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileScreenState
        ): StudentProfileScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    profileState = StudentProfileScreenState.DisplayState.ProfileState.Loading
                )
            )
        }
    }

    data class ProfileLoaded(
        val profile: StudentProfile,
    ) : StudentProfileScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileScreenState
        ): StudentProfileScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    profileState = StudentProfileScreenState.DisplayState.ProfileState
                        .Available(profile),
                )
            )
        }
    }

    data class ProfileLoadFailed(val message: String) : StudentProfileScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileScreenState
        ): StudentProfileScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    profileState = StudentProfileScreenState.DisplayState.ProfileState
                        .Error(message)
                )
            )
        }
    }

    data class UpdateMessageState(
        val messageState: StudentProfileScreenState.DisplayState.MessageState
    ) : StudentProfileScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileScreenState
        ): StudentProfileScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : StudentProfileScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileScreenState
        ): StudentProfileScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: StudentProfileScreenState.NavigationState,
    ) : StudentProfileScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileScreenState,
        ): StudentProfileScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : StudentProfileScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileScreenState,
        ): StudentProfileScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}