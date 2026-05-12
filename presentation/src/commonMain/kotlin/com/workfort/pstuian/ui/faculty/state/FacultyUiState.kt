package com.workfort.pstuian.ui.faculty.state

sealed interface FacultyUiState {

    val title: String
    val showOperationLoading: Boolean

    data class None(
        override val title: String = "",
        override val showOperationLoading: Boolean = false
    ) : FacultyUiState

    data class Content(
        override val title: String,
        override val showOperationLoading: Boolean = false,
        val facultyId: Int,
        val tabs: List<String> = mutableListOf(),
        val selectedTab: Int = 0,
    ) : FacultyUiState
}
