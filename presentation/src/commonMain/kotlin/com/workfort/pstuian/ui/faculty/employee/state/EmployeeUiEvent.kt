package com.workfort.pstuian.ui.faculty.employee.state

import com.workfort.pstuian.featuredomain.model.User

sealed interface EmployeeUiEvent {
    data object LoadMore : EmployeeUiEvent
    data class EmployeeClicked(val employee: User.Employee) : EmployeeUiEvent
    data class CallClicked(val phoneNumber: String) : EmployeeUiEvent
}
