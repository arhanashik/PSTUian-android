package com.workfort.pstuian.ui.profile.employeeprofile.state

sealed interface EmployeeProfileMessageState {
    data class Loading(val cancelable: Boolean) : EmployeeProfileMessageState
    data class InputBio(
        val currentBio: String,
        val onConfirm: (String) -> Unit,
    ) : EmployeeProfileMessageState
    data class CallConfirmation(
        val phoneNumber: String,
        val onConfirm: () -> Unit,
    ) : EmployeeProfileMessageState
    data class EmailConfirmation(
        val email: String,
        val onConfirm: () -> Unit,
    ) : EmployeeProfileMessageState
    data class ConfirmSignOut(val onConfirm: () -> Unit) : EmployeeProfileMessageState
    data class Success(val message: String) : EmployeeProfileMessageState
    data class Error(val message: String) : EmployeeProfileMessageState
}