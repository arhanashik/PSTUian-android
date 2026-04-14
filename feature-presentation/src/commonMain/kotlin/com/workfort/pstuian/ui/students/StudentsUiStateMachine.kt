package com.workfort.pstuian.ui.students

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.StudentEntity
import com.workfort.pstuian.ui.students.state.StudentsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class StudentsUiStateMachine : UiStateMachine<StudentsUiState> {

    private val _uiState = MutableStateFlow(StudentsUiState())
    override val uiState: StateFlow<StudentsUiState> = _uiState.asStateFlow()

    fun showLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun showContent(items: List<StudentEntity>) {
        _uiState.update { it.copy(items = items, isLoading = false, error = null) }
    }

    fun showError(message: String) {
        _uiState.update { it.copy(error = message, isLoading = false) }
    }
}
