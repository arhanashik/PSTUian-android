package com.workfort.pstuian.ui.faculty.state

import com.workfort.pstuian.featuredomain.model.BatchEntity
import com.workfort.pstuian.featuredomain.model.CourseEntity
import com.workfort.pstuian.featuredomain.model.EmployeeEntity
import com.workfort.pstuian.featuredomain.model.TeacherEntity

sealed interface FacultyUiEvent {
    data object BackClicked : FacultyUiEvent
    data class SelectTab(val index: Int) : FacultyUiEvent
    data class BatchClicked(val batch: BatchEntity) : FacultyUiEvent
    data class TeacherClicked(val teacher: TeacherEntity) : FacultyUiEvent
    data class CourseClicked(val course: CourseEntity) : FacultyUiEvent
    data class EmployeeClicked(val employee: EmployeeEntity) : FacultyUiEvent
    data class CallClicked(val phoneNumber: String) : FacultyUiEvent
}
