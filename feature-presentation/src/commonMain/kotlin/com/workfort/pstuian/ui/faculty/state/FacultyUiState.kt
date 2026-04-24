package com.workfort.pstuian.ui.faculty.state

import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.CourseEntity
import com.workfort.pstuian.featuredomain.model.User

data class FacultyUiState(
    val title: String = "Faculty",
    val tabs: MutableList<String> = mutableListOf(),
    val selectedTab: Int = 0,
    val batchListState: BatchListState = BatchListState(),
    val teacherListState: TeacherListState = TeacherListState(),
    val courseListState: CourseListState = CourseListState(),
    val employeeListState: EmployeeListState = EmployeeListState(),
) {
    data class BatchListState(
        val batches: List<Batch> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    data class TeacherListState(
        val teachers: List<User.Teacher> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    data class CourseListState(
        val courses: List<CourseEntity> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    data class EmployeeListState(
        val employees: List<User.Employee> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
    )
}
