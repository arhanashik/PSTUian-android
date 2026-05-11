package com.workfort.pstuian.ui.faculty

import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.Course
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.faculty.state.FacultyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FacultyUiStateMachine : UiStateMachine<FacultyUiState> {
    private val _uiState = MutableStateFlow<FacultyUiState>(FacultyUiState.None())
    override val uiState: StateFlow<FacultyUiState> = _uiState.asStateFlow()

    fun setInitialContent(
        title: String,
        tabs: List<String>,
        selectedTab: Int,
    ) {
        _uiState.update {
            FacultyUiState.Content(
                showOperationLoading = false,
                title = title,
                tabs = tabs,
                selectedTab = selectedTab,
            )
        }
    }

    fun showLoadingOverlay(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is FacultyUiState.None -> current.copy(showOperationLoading = isLoading)
                is FacultyUiState.Content -> current.copy(showOperationLoading = isLoading)
            }
        }
    }

    fun selectTab(index: Int) {
        _uiState.update { current ->
            when (current) {
                is FacultyUiState.Content -> current.copy(selectedTab = index)
                else -> current
            }
        }
    }

    fun updateBatchList(
        isLoading: Boolean = false,
        batches: List<Batch> = emptyList(),
        error: String? = null,
    ) {
        _uiState.update { current ->
            when (current) {
                is FacultyUiState.Content -> current.copy(
                    batchListState = FacultyUiState.BatchListState(isLoading, batches, error),
                )
                else -> current
            }
        }
    }

    fun updateTeacherList(
        isLoading: Boolean = false,
        teachers: List<User.Teacher> = emptyList(),
        error: String? = null,
    ) {
        _uiState.update { current ->
            when (current) {
                is FacultyUiState.Content -> current.copy(
                    teacherListState = FacultyUiState.TeacherListState(isLoading, teachers, error),
                )
                else -> current
            }
        }
    }

    fun updateCourseList(
        isLoading: Boolean = false,
        courses: List<Course> = emptyList(),
        error: String? = null,
    ) {
        _uiState.update { current ->
            when (current) {
                is FacultyUiState.Content -> current.copy(
                    courseListState = FacultyUiState.CourseListState(isLoading, courses, error),
                )
                else -> current
            }
        }
    }

    fun updateEmployeeList(
        isLoading: Boolean = false,
        employees: List<User.Employee> = emptyList(),
        error: String? = null,
    ) {
        _uiState.update { current ->
            when (current) {
                is FacultyUiState.Content -> current.copy(
                    employeeListState = FacultyUiState.EmployeeListState(isLoading, employees, error),
                )
                else -> current
            }
        }
    }
}
