package com.workfort.pstuian.ui.students.state

import com.workfort.pstuian.featuredomain.model.StudentEntity

sealed interface StudentsUiEvent {
    data object LoadStudentList : StudentsUiEvent
    data object ClickBack : StudentsUiEvent
    data class ClickStudent(val student: StudentEntity) : StudentsUiEvent
    data class ClickCall(val phoneNumber: String) : StudentsUiEvent
    data class Call(val phoneNumber: String) : StudentsUiEvent
    data object MessageConsumed : StudentsUiEvent
    data object NavigationConsumed : StudentsUiEvent
}
