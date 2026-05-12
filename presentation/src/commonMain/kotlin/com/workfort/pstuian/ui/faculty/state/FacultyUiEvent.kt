package com.workfort.pstuian.ui.faculty.state

import com.workfort.pstuian.featuredomain.model.Course
import com.workfort.pstuian.featuredomain.model.User

sealed interface FacultyUiEvent {
    data object BackClicked : FacultyUiEvent
    data class SelectTab(val index: Int) : FacultyUiEvent
    data class TeacherClicked(val teacher: User.Teacher) : FacultyUiEvent
    data class CourseClicked(val course: Course) : FacultyUiEvent
    data class EmployeeClicked(val employee: User.Employee) : FacultyUiEvent
    data class CallClicked(val phoneNumber: String) : FacultyUiEvent
}
