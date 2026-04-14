package com.workfort.pstuian.ui.students.state

import com.workfort.pstuian.featuredomain.model.StudentEntity

sealed interface NavigationState {
    data object GoBack : NavigationState
    data class GoToStudentProfile(val student: StudentEntity) : NavigationState
}
