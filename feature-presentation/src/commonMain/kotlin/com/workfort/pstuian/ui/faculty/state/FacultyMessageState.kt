package com.workfort.pstuian.ui.faculty.state

sealed interface FacultyMessageState {
    data class ConfirmCall(
        val phoneNumber: String,
        val onConfirm: () -> Unit,
    ) : FacultyMessageState
    data class ShowError(
        val title: String = "Error",
        val message: String,
        val onRetry: () -> Unit,
    ) : FacultyMessageState
}
