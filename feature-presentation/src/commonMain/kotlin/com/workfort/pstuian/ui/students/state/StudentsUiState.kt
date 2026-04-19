package com.workfort.pstuian.ui.students.state

import com.workfort.pstuian.featuredomain.model.User

data class StudentsUiState(
    val title: String = "Students",
    val items: List<User.Student> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
