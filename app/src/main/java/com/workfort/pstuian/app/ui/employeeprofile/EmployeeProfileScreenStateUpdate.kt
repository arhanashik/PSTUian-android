package com.workfort.pstuian.app.ui.employeeprofile

import com.workfort.pstuian.model.EmployeeProfile
import com.workfort.pstuian.view.service.StateUpdate

sealed interface EmployeeProfileScreenStateUpdate : StateUpdate<EmployeeProfileScreenState> {

    data class UpdateSelectedTab(val index: Int) : EmployeeProfileScreenStateUpdate {
        override fun invoke(
            oldState: EmployeeProfileScreenState,
        ): EmployeeProfileScreenState = with(oldState) {
            copy(displayState = displayState.copy(selectedTabIndex = index))
        }
    }

    data object ProfileLoading : EmployeeProfileScreenStateUpdate {
        override fun invoke(
            oldState: EmployeeProfileScreenState,
        ): EmployeeProfileScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    profileState = EmployeeProfileScreenState.DisplayState.ProfileState.Loading
                )
            )
        }
    }

    data class ProfileLoaded(
        val profile: EmployeeProfile,
    ) : EmployeeProfileScreenStateUpdate {
        override fun invoke(
            oldState: EmployeeProfileScreenState
        ): EmployeeProfileScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    profileState = EmployeeProfileScreenState.DisplayState.ProfileState
                        .Available(profile)
                )
            )
        }
    }

    data class ProfileLoadFailed(val message: String) : EmployeeProfileScreenStateUpdate {
        override fun invoke(
            oldState: EmployeeProfileScreenState
        ): EmployeeProfileScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    profileState = EmployeeProfileScreenState.DisplayState.ProfileState
                        .Error(message)
                )
            )
        }
    }

    data class UpdateMessageState(
        val messageState: EmployeeProfileScreenState.DisplayState.MessageState
    ) : EmployeeProfileScreenStateUpdate {
        override fun invoke(
            oldState: EmployeeProfileScreenState
        ): EmployeeProfileScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : EmployeeProfileScreenStateUpdate {
        override fun invoke(
            oldState: EmployeeProfileScreenState
        ): EmployeeProfileScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: EmployeeProfileScreenState.NavigationState,
    ) : EmployeeProfileScreenStateUpdate {
        override fun invoke(
            oldState: EmployeeProfileScreenState,
        ): EmployeeProfileScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : EmployeeProfileScreenStateUpdate {
        override fun invoke(
            oldState: EmployeeProfileScreenState,
        ): EmployeeProfileScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}