package com.workfort.pstuian.ui.faculty.faculty.state

sealed interface FacultyUiEvent {
    data object BackClicked : FacultyUiEvent
    data class SelectTab(val index: Int) : FacultyUiEvent
}
