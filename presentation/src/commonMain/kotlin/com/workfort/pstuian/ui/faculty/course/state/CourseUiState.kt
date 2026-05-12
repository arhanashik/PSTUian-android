package com.workfort.pstuian.ui.faculty.course.state

import com.workfort.pstuian.featuredomain.model.Course

sealed interface CourseUiState {

    data object None : CourseUiState

    data class Content(
        val isLoading: Boolean = false,
        val courses: List<Course> = emptyList(),
    ) : CourseUiState

    data class Error(val error: String? = null) : CourseUiState
}
