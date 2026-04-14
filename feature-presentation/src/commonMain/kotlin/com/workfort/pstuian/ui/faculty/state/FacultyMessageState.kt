package com.workfort.pstuian.ui.faculty.state

sealed interface FacultyMessageState {
    data class ConfirmCall(
        val phoneNumber: String,
        val onConfirm: () -> Unit,
    ) : FacultyMessageState
}
