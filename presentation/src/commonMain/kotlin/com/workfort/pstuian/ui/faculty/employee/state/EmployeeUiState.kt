package com.workfort.pstuian.ui.faculty.employee.state

import com.workfort.pstuian.featuredomain.model.User

sealed interface EmployeeUiState {

    data object None : EmployeeUiState

    data class Content(
        val isLoading: Boolean = false,
        val employees: List<User.Employee> = emptyList(),
    ) : EmployeeUiState

    data class Error(val error: String? = null) : EmployeeUiState
}
