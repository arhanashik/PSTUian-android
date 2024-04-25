package com.workfort.pstuian.app.ui.students

import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.view.service.StateUpdate

sealed interface StudentsScreenStateUpdate : StateUpdate<StudentsScreenState> {

    data class UpdateTitle(val title: String) : StudentsScreenStateUpdate {
        override fun invoke(oldState: StudentsScreenState): StudentsScreenState = with(oldState) {
            copy(displayState = displayState.copy(title = title))
        }
    }

    data class UpdateStudentListData(
        val students: List<StudentEntity>,
        val isLoading: Boolean,
    ) : StudentsScreenStateUpdate {
        override fun invoke(
            oldState: StudentsScreenState
        ): StudentsScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    studentListState = StudentsScreenState.DisplayState.StudentListState
                        .Available(students, isLoading)
                )
            )
        }
    }

    data class StudentListLoadFailed(val message: String) : StudentsScreenStateUpdate {
        override fun invoke(
            oldState: StudentsScreenState
        ): StudentsScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    studentListState = StudentsScreenState.DisplayState.StudentListState
                        .Error(message)
                )
            )
        }
    }

    data class UpdateMessageState(
        val messageState: StudentsScreenState.DisplayState.MessageState
    ) : StudentsScreenStateUpdate {
        override fun invoke(
            oldState: StudentsScreenState
        ): StudentsScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : StudentsScreenStateUpdate {
        override fun invoke(
            oldState: StudentsScreenState
        ): StudentsScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: StudentsScreenState.NavigationState,
    ) : StudentsScreenStateUpdate {
        override fun invoke(
            oldState: StudentsScreenState,
        ): StudentsScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : StudentsScreenStateUpdate {
        override fun invoke(
            oldState: StudentsScreenState,
        ): StudentsScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}