package com.workfort.pstuian.ui.faculty.state

sealed interface FacultyNavigationState {
    data object GoBack : FacultyNavigationState
    data class GoToTeacherProfileScreen(val userId: Int) : FacultyNavigationState
    data class GoToEmployeeProfileScreen(val userId: Int) : FacultyNavigationState
}
