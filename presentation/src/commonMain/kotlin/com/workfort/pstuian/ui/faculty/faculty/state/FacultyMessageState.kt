package com.workfort.pstuian.ui.faculty.faculty.state

sealed interface FacultyMessageState {
    data class ShowError(
        val title: String = "Error",
        val message: String,
        val onRetry: () -> Unit,
    ) : FacultyMessageState
}
