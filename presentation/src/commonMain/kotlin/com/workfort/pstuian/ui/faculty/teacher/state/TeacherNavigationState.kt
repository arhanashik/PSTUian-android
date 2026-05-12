package com.workfort.pstuian.ui.faculty.teacher.state

sealed interface TeacherNavigationState {
    data class GoToTeacherProfile(val userId: Int) : TeacherNavigationState
}
