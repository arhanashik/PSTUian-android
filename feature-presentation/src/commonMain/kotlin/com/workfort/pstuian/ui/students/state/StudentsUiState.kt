package com.workfort.pstuian.ui.students.state

import com.workfort.pstuian.featuredomain.model.StudentEntity

data class StudentsUiState(
    val title: String = "Students",
    val items: List<StudentEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val messageState: MessageState? = null,
    val navigationState: NavigationState? = null,
)
