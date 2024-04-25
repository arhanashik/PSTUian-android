package com.workfort.pstuian.app.ui.teacherprofile

import com.workfort.pstuian.model.TeacherProfile
import com.workfort.pstuian.view.service.StateUpdate

sealed interface TeacherProfileScreenStateUpdate : StateUpdate<TeacherProfileScreenState> {

    data class UpdateSelectedTab(val index: Int) : TeacherProfileScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileScreenState,
        ): TeacherProfileScreenState = with(oldState) {
            copy(displayState = displayState.copy(selectedTabIndex = index))
        }
    }

    data class UpdateSignedInState(val isSignedIn: Boolean) : TeacherProfileScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileScreenState,
        ): TeacherProfileScreenState = with(oldState) {
            copy(displayState = displayState.copy(isSignedIn = isSignedIn))
        }
    }

    data object ProfileLoading : TeacherProfileScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileScreenState
        ): TeacherProfileScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    profileState = TeacherProfileScreenState.DisplayState.ProfileState.Loading
                )
            )
        }
    }

    data class ProfileLoaded(
        val profile: TeacherProfile,
    ) : TeacherProfileScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileScreenState
        ): TeacherProfileScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    profileState = TeacherProfileScreenState.DisplayState.ProfileState
                        .Available(profile),
                )
            )
        }
    }

    data class ProfileLoadFailed(val message: String) : TeacherProfileScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileScreenState
        ): TeacherProfileScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    profileState = TeacherProfileScreenState.DisplayState.ProfileState
                        .Error(message)
                )
            )
        }
    }

    data class UpdateMessageState(
        val messageState: TeacherProfileScreenState.DisplayState.MessageState
    ) : TeacherProfileScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileScreenState
        ): TeacherProfileScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : TeacherProfileScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileScreenState
        ): TeacherProfileScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: TeacherProfileScreenState.NavigationState,
    ) : TeacherProfileScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileScreenState,
        ): TeacherProfileScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : TeacherProfileScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileScreenState,
        ): TeacherProfileScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}