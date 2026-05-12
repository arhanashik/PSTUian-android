package com.workfort.pstuian.ui.faculty.teacher.state

import com.workfort.pstuian.featuredomain.model.User

sealed interface TeacherUiState {

    data object None : TeacherUiState

    data class Content(
        val isLoading: Boolean = false,
        val teachers: List<User.Teacher> = emptyList(),
    ) : TeacherUiState

    data class Error(val error: String? = null) : TeacherUiState
}
