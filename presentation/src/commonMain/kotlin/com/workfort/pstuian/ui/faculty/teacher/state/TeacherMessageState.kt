package com.workfort.pstuian.ui.faculty.teacher.state

sealed interface TeacherMessageState {
    data class ConfirmCall(
        val phoneNumber: String,
        val onConfirm: () -> Unit,
    ) : TeacherMessageState
}
