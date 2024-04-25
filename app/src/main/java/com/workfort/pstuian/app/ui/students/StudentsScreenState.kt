package com.workfort.pstuian.app.ui.students

import com.workfort.pstuian.model.StudentEntity


data class StudentsScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val title: String,
        val studentListState: StudentListState,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                title = "",
                studentListState = StudentListState.None,
                messageState = null,
            )
        }
        sealed interface StudentListState {
            data object None : StudentListState
            data class Available(
                val students: List<StudentEntity>,
                val isLoading: Boolean,
            ) : StudentListState
            data class Error(val message: String) : StudentListState
        }
        sealed interface MessageState {
            data class Call(val phoneNumber: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
        data class GoToStudentProfile(val student: StudentEntity) : NavigationState
    }
}