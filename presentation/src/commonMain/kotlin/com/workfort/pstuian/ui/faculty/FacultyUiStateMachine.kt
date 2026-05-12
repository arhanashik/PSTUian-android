package com.workfort.pstuian.ui.faculty

import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.faculty.state.FacultyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FacultyUiStateMachine : UiStateMachine<FacultyUiState> {
    private val _uiState = MutableStateFlow<FacultyUiState>(FacultyUiState.None())
    override val uiState: StateFlow<FacultyUiState> = _uiState.asStateFlow()

    fun setInitialContent(
        facultyId: Int,
        title: String,
        tabs: List<String>,
        selectedTab: Int,
    ) {
        _uiState.update {
            FacultyUiState.Content(
                showOperationLoading = false,
                facultyId = facultyId,
                title = title,
                tabs = tabs,
                selectedTab = selectedTab,
            )
        }
    }

    fun showLoadingOverlay(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is FacultyUiState.None -> current.copy(showOperationLoading = isLoading)
                is FacultyUiState.Content -> current.copy(showOperationLoading = isLoading)
            }
        }
    }

    fun selectTab(index: Int) {
        _uiState.update { current ->
            when (current) {
                is FacultyUiState.Content -> current.copy(selectedTab = index)
                else -> current
            }
        }
    }
}
