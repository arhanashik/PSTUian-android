package com.workfort.pstuian.ui.students.state

import com.workfort.pstuian.featuredomain.model.StudentEntity

sealed interface StudentsUiEvent {
    data object LoadStudentList : StudentsUiEvent
    data object BackClicked : StudentsUiEvent
    data class StudentClicked(val student: StudentEntity) : StudentsUiEvent
    data class CallClicked(val phoneNumber: String) : StudentsUiEvent
}
