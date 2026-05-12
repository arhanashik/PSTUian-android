package com.workfort.pstuian.ui.faculty.employee.state

sealed interface EmployeeNavigationState {
    data class GoToEmployeeProfile(val userId: Int) : EmployeeNavigationState
}
