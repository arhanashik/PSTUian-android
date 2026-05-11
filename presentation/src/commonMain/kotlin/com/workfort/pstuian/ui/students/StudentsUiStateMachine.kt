package com.workfort.pstuian.ui.students

import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.students.state.StudentsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StudentsUiStateMachine : UiStateMachine<StudentsUiState> {

    private val _uiState = MutableStateFlow<StudentsUiState>(StudentsUiState.None)
    override val uiState: StateFlow<StudentsUiState> = _uiState.asStateFlow()

    fun showInitialState(title: String) {
        _uiState.update { StudentsUiState.Content(title = title) }
    }

    fun showOperationLoading() {
        _uiState.update { StudentsUiState.Loading }
    }

    fun showContentLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is StudentsUiState.Content -> current.copy(isLoading = isLoading)
                else -> current
            }
        }
    }

    fun showStudents(students: List<User.Student>) {
        _uiState.update { current ->
            when (current) {
                is StudentsUiState.Content -> current.copy(students = students, isLoading = false)
                else -> current
            }
        }
    }

    fun showError(error: String) {
        _uiState.update { StudentsUiState.Error(error) }
    }
}
