package com.workfort.pstuian.ui.faculty

import com.workfort.pstuian.featuredomain.model.BatchEntity
import com.workfort.pstuian.featuredomain.model.CourseEntity
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.faculty.state.FacultyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FacultyUiStateMachine : UiStateMachine<FacultyUiState> {
    private val _uiState = MutableStateFlow(FacultyUiState())
    override val uiState: StateFlow<FacultyUiState> = _uiState.asStateFlow()

    fun setInitialContent(title: String, tabs: List<String>, selectedTab: Int) {
        _uiState.update {
            it.copy(title = title, tabs = tabs as MutableList<String>, selectedTab = selectedTab)
        }
    }

    fun selectTab(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun updateBatchList(batches: List<BatchEntity>, isLoading: Boolean, error: String? = null) {
        _uiState.update {
            it.copy(
                batchListState = it.batchListState.copy(
                    batches = batches,
                    isLoading = isLoading,
                    error = error
                )
            )
        }
    }

    fun updateTeacherList(teachers: List<User.Teacher>, isLoading: Boolean, error: String? = null) {
        _uiState.update {
            it.copy(
                teacherListState = it.teacherListState.copy(
                    teachers = teachers,
                    isLoading = isLoading,
                    error = error
                )
            )
        }
    }

    fun updateCourseList(courses: List<CourseEntity>, isLoading: Boolean, error: String? = null) {
        _uiState.update {
            it.copy(
                courseListState = it.courseListState.copy(
                    courses = courses,
                    isLoading = isLoading,
                    error = error
                )
            )
        }
    }

    fun updateEmployeeList(employees: List<User.Employee>, isLoading: Boolean, error: String? = null) {
        _uiState.update {
            it.copy(
                employeeListState = it.employeeListState.copy(
                    employees = employees,
                    isLoading = isLoading,
                    error = error
                )
            )
        }
    }
}
