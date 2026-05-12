package com.workfort.pstuian.ui.faculty.teacher.state

import com.workfort.pstuian.featuredomain.model.User

sealed interface TeacherUiEvent {
    data object LoadMore : TeacherUiEvent
    data class TeacherClicked(val teacher: User.Teacher) : TeacherUiEvent
    data class CallClicked(val phoneNumber: String) : TeacherUiEvent
}
