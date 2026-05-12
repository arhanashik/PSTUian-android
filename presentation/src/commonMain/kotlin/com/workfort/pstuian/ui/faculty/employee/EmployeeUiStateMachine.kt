package com.workfort.pstuian.ui.faculty.employee

import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.faculty.employee.state.EmployeeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EmployeeUiStateMachine : UiStateMachine<EmployeeUiState> {

    private val _uiState = MutableStateFlow<EmployeeUiState>(EmployeeUiState.None)
    override val uiState: StateFlow<EmployeeUiState> = _uiState.asStateFlow()

    fun showContentLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is EmployeeUiState.Content -> current.copy(isLoading = isLoading)
                else -> EmployeeUiState.Content(isLoading = isLoading)
            }
        }
    }

    fun showEmployees(employees: List<User.Employee>) {
        _uiState.update { current ->
            when (current) {
                is EmployeeUiState.Content -> current.copy(employees = employees, isLoading = false)
                else -> EmployeeUiState.Content(employees = employees, isLoading = false)
            }
        }
    }

    fun showError(error: String) {
        _uiState.update { EmployeeUiState.Error(error) }
    }
}
