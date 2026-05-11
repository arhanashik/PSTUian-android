package com.workfort.pstuian.ui.students.state

import com.workfort.pstuian.featuredomain.model.User

sealed interface StudentsUiState {

    data object None : StudentsUiState

    data object Loading : StudentsUiState

    data class Content(
        val title: String = "",
        val isLoading: Boolean = false,
        val students: List<User.Student> = emptyList(),
    ) : StudentsUiState

    data class Error(val error: String? = null) : StudentsUiState
}
