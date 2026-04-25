package com.workfort.pstuian.ui.students.state

import com.workfort.pstuian.featuredomain.model.User

sealed interface StudentsUiEvent {
    data object BackClicked : StudentsUiEvent
    data object Refresh : StudentsUiEvent
    data object LoadMore : StudentsUiEvent
    data class StudentClicked(val student: User.Student) : StudentsUiEvent
    data class CallClicked(val phoneNumber: String) : StudentsUiEvent
}
