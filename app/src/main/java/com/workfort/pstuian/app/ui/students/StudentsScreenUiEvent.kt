package com.workfort.pstuian.app.ui.students

import com.workfort.pstuian.model.StudentEntity


sealed class StudentsScreenUiEvent {
    data object None : StudentsScreenUiEvent()
    data object LoadStudentList : StudentsScreenUiEvent()
    data object OnClickBack : StudentsScreenUiEvent()
    data class OnClickStudent(val student: StudentEntity) : StudentsScreenUiEvent()
    data class OnClickCall(val phoneNumber: String) : StudentsScreenUiEvent()
    data class OnCall(val phoneNumber: String) : StudentsScreenUiEvent()
    data object MessageConsumed : StudentsScreenUiEvent()
    data object NavigationConsumed : StudentsScreenUiEvent()
}