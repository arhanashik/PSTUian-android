package com.workfort.pstuian.ui.faculty.state

sealed interface FacultyNavigationState {
    data object GoBack : FacultyNavigationState
    data class GoToStudentsScreen(val batchId: Int) : FacultyNavigationState
    data class GoToTeacherScreen(val userId: Int) : FacultyNavigationState
    data class GoToEmployeeScreen(val userId: Int) : FacultyNavigationState
}
