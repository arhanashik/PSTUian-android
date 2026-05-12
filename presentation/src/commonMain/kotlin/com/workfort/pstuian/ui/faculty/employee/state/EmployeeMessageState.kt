package com.workfort.pstuian.ui.faculty.employee.state

sealed interface EmployeeMessageState {
    data class ConfirmCall(
        val phoneNumber: String,
        val onConfirm: () -> Unit,
    ) : EmployeeMessageState
}
