package com.workfort.pstuian.app.ui.teacherprofileedit

import com.workfort.pstuian.model.TeacherAcademicInfoInputError
import com.workfort.pstuian.model.TeacherConnectInfoInputError
import com.workfort.pstuian.model.TeacherProfile
import com.workfort.pstuian.view.service.StateUpdate

sealed interface TeacherProfileEditScreenStateUpdate : StateUpdate<TeacherProfileEditScreenState> {

    data object ProfileLoading : TeacherProfileEditScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileEditScreenState,
        ): TeacherProfileEditScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    panelState = TeacherProfileEditScreenState.DisplayState.PanelState.Loading,
                ),
            )
        }
    }

    data class ProfileLoadedForAcademic(
        val profile: TeacherProfile,
        val validationError: TeacherAcademicInfoInputError,
    ) : TeacherProfileEditScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileEditScreenState,
        ): TeacherProfileEditScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    panelState = TeacherProfileEditScreenState.DisplayState.PanelState.Academic(
                        profile = profile,
                        validationError = validationError,
                    ),
                ),
            )
        }
    }

    data class ProfileLoadedForConnect(
        val profile: TeacherProfile,
        val validationError: TeacherConnectInfoInputError,
    ) : TeacherProfileEditScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileEditScreenState,
        ): TeacherProfileEditScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    panelState = TeacherProfileEditScreenState.DisplayState.PanelState.Connect(
                        profile = profile,
                        validationError = validationError,
                    ),
                ),
            )
        }
    }

    data class ProfileLoadFailed(val message: String) : TeacherProfileEditScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileEditScreenState,
        ): TeacherProfileEditScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    panelState = TeacherProfileEditScreenState.DisplayState.PanelState
                        .Error(message)
                )
            )
        }
    }

    data class UpdateMessageState(
        val messageState: TeacherProfileEditScreenState.DisplayState.MessageState,
    ) : TeacherProfileEditScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileEditScreenState
        ): TeacherProfileEditScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : TeacherProfileEditScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileEditScreenState
        ): TeacherProfileEditScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: TeacherProfileEditScreenState.NavigationState,
    ) : TeacherProfileEditScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileEditScreenState,
        ): TeacherProfileEditScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : TeacherProfileEditScreenStateUpdate {
        override fun invoke(
            oldState: TeacherProfileEditScreenState,
        ): TeacherProfileEditScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}