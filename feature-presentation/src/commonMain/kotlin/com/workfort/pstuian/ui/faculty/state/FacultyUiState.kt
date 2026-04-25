package com.workfort.pstuian.ui.faculty.state

import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.Course
import com.workfort.pstuian.featuredomain.model.User

sealed interface FacultyUiState {

    val title: String
    val showOperationLoading: Boolean

    data class None(
        override val title: String = "",
        override val showOperationLoading: Boolean = false
    ) : FacultyUiState

    data class Content(
        override val title: String,
        override val showOperationLoading: Boolean = false,
        val tabs: List<String> = mutableListOf(),
        val selectedTab: Int = 0,
        val batchListState: BatchListState = BatchListState(),
        val teacherListState: TeacherListState = TeacherListState(),
        val courseListState: CourseListState = CourseListState(),
        val employeeListState: EmployeeListState = EmployeeListState(),
    ) : FacultyUiState

    data class BatchListState(
        val isLoading: Boolean = false,
        val batches: List<Batch> = emptyList(),
        val error: String? = null,
    )

    data class TeacherListState(
        val isLoading: Boolean = false,
        val teachers: List<User.Teacher> = emptyList(),
        val error: String? = null,
    )

    data class CourseListState(
        val isLoading: Boolean = false,
        val courses: List<Course> = emptyList(),
        val error: String? = null,
    )

    data class EmployeeListState(
        val isLoading: Boolean = false,
        val employees: List<User.Employee> = emptyList(),
        val error: String? = null,
    )
}
