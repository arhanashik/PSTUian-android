package com.workfort.pstuian.app.ui.studentprofileedit

import com.workfort.pstuian.model.StudentAcademicInfoInputError
import com.workfort.pstuian.model.StudentConnectInfoInputError
import com.workfort.pstuian.model.StudentProfile
import com.workfort.pstuian.view.service.StateUpdate

sealed interface StudentProfileEditScreenStateUpdate : StateUpdate<StudentProfileEditScreenState> {

    data object ProfileLoading : StudentProfileEditScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileEditScreenState,
        ): StudentProfileEditScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    panelState = StudentProfileEditScreenState.DisplayState.PanelState.Loading,
                ),
            )
        }
    }

    data class ProfileLoadedForAcademic(
        val profile: StudentProfile,
        val validationError: StudentAcademicInfoInputError,
    ) : StudentProfileEditScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileEditScreenState,
        ): StudentProfileEditScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    panelState = StudentProfileEditScreenState.DisplayState.PanelState.Academic(
                        profile = profile,
                        validationError = validationError,
                    ),
                ),
            )
        }
    }

    data class ProfileLoadedForConnect(
        val profile: StudentProfile,
        val validationError: StudentConnectInfoInputError,
    ) : StudentProfileEditScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileEditScreenState,
        ): StudentProfileEditScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    panelState = StudentProfileEditScreenState.DisplayState.PanelState.Connect(
                        profile = profile,
                        validationError = validationError,
                    ),
                ),
            )
        }
    }

    data class ProfileLoadFailed(val message: String) : StudentProfileEditScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileEditScreenState,
        ): StudentProfileEditScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    panelState = StudentProfileEditScreenState.DisplayState.PanelState
                        .Error(message)
                )
            )
        }
    }

    data class UpdateMessageState(
        val messageState: StudentProfileEditScreenState.DisplayState.MessageState,
    ) : StudentProfileEditScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileEditScreenState
        ): StudentProfileEditScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : StudentProfileEditScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileEditScreenState
        ): StudentProfileEditScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: StudentProfileEditScreenState.NavigationState,
    ) : StudentProfileEditScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileEditScreenState,
        ): StudentProfileEditScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : StudentProfileEditScreenStateUpdate {
        override fun invoke(
            oldState: StudentProfileEditScreenState,
        ): StudentProfileEditScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}