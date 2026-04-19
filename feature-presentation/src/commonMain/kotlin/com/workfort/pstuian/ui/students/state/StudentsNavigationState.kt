package com.workfort.pstuian.ui.students.state

sealed interface StudentsNavigationState {
    data object GoBack : StudentsNavigationState
    data class GoToStudentProfile(val studentId: String) : StudentsNavigationState
}
