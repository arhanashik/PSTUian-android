package com.workfort.pstuian.ui.students.state

sealed interface StudentsMessageState {
    data class ConfirmCall(val phoneNumber: String, val onConfirm: () -> Unit) : StudentsMessageState
}
