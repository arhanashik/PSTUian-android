package com.workfort.pstuian.ui.faculty.course

import com.workfort.pstuian.featuredomain.model.Course
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.faculty.course.state.CourseUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CourseUiStateMachine : UiStateMachine<CourseUiState> {

    private val _uiState = MutableStateFlow<CourseUiState>(CourseUiState.None)
    override val uiState: StateFlow<CourseUiState> = _uiState.asStateFlow()

    fun showContentLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is CourseUiState.Content -> current.copy(isLoading = isLoading)
                else -> CourseUiState.Content(isLoading = isLoading)
            }
        }
    }

    fun showCourses(courses: List<Course>) {
        _uiState.update { current ->
            when (current) {
                is CourseUiState.Content -> current.copy(courses = courses, isLoading = false)
                else -> CourseUiState.Content(courses = courses, isLoading = false)
            }
        }
    }

    fun showError(error: String) {
        _uiState.update { CourseUiState.Error(error) }
    }
}
