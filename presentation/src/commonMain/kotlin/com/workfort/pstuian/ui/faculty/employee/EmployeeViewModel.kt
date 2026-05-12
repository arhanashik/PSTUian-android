package com.workfort.pstuian.ui.faculty.employee

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.faculty.employee.state.EmployeeMessageState
import com.workfort.pstuian.ui.faculty.employee.state.EmployeeNavigationState
import com.workfort.pstuian.ui.faculty.employee.state.EmployeeUiEvent
import com.workfort.pstuian.ui.faculty.employee.state.EmployeeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EmployeeViewModel(
    private val facultyId: Int,
    private val facultyRepo: FacultyRepository,
    private val uiStateMachine: EmployeeUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<EmployeeUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<EmployeeMessageState?>(null)
    val message: StateFlow<EmployeeMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<EmployeeNavigationState?>(null)
    val navigation: StateFlow<EmployeeNavigationState?> = _navigation.asStateFlow()

    private val employeeListCache = mutableListOf<User.Employee>()
    private var currentPage = 1
    private var hasMoreData = true

    override fun onUiReady() {
        getEmployees(forceRefresh = false)
    }

    fun onUiEvent(event: EmployeeUiEvent) {
        when (event) {
            is EmployeeUiEvent.LoadMore -> getEmployees(forceRefresh = false)
            is EmployeeUiEvent.EmployeeClicked -> onClickEmployee(event.employee)
            is EmployeeUiEvent.CallClicked -> onClickCall(event.phoneNumber)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickEmployee(employee: User.Employee) {
        _navigation.update { EmployeeNavigationState.GoToEmployeeProfile(employee.userId) }
    }

    private fun onClickCall(phoneNumber: String) {
        _message.update {
            EmployeeMessageState.ConfirmCall(phoneNumber) {
                // Do calling
            }
        }
    }

    private fun getEmployees(forceRefresh: Boolean) {
        if (forceRefresh) {
            employeeListCache.clear()
            currentPage = 1
            hasMoreData = true
        } else if (!hasMoreData) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.showContentLoading(isLoading = true)
            facultyRepo.getEmployees(facultyId, currentPage, forceRefresh)
                .onSuccess { employees ->
                    if (employees.isEmpty()) {
                        hasMoreData = false
                    } else {
                        currentPage++
                    }
                    employeeListCache.addAll(employees)
                    uiStateMachine.showEmployees(employeeListCache.toList())
                }
                .onFailure {
                    uiStateMachine.showContentLoading(isLoading = false)
                    if (employeeListCache.isEmpty()) {
                        val message = it.message ?: "Failed to load employees"
                        uiStateMachine.showError(message)
                    }
                }
        }
    }
}
