package com.workfort.pstuian.ui.faculty.state

sealed interface FacultyUiState {

    data object None : FacultyUiState

    data object Loading : FacultyUiState

    data class Content(
        val title: String,
        val facultyId: Int,
        val tabs: List<String> = mutableListOf(),
        val selectedTab: Int = 0,
    ) : FacultyUiState
}
