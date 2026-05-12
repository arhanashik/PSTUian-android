package com.workfort.pstuian.ui.faculty.teacher

import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.faculty.teacher.state.TeacherUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TeacherUiStateMachine : UiStateMachine<TeacherUiState> {

    private val _uiState = MutableStateFlow<TeacherUiState>(TeacherUiState.None)
    override val uiState: StateFlow<TeacherUiState> = _uiState.asStateFlow()

    fun showContentLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is TeacherUiState.Content -> current.copy(isLoading = isLoading)
                else -> TeacherUiState.Content(isLoading = isLoading)
            }
        }
    }

    fun showTeachers(teachers: List<User.Teacher>) {
        _uiState.update { current ->
            when (current) {
                is TeacherUiState.Content -> current.copy(teachers = teachers, isLoading = false)
                else -> TeacherUiState.Content(teachers = teachers, isLoading = false)
            }
        }
    }

    fun showError(error: String) {
        _uiState.update { TeacherUiState.Error(error) }
    }
}
