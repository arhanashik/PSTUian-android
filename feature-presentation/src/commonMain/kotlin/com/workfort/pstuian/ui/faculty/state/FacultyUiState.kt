package com.workfort.pstuian.ui.faculty.state

import com.workfort.pstuian.featuredomain.model.BatchEntity
import com.workfort.pstuian.featuredomain.model.CourseEntity
import com.workfort.pstuian.featuredomain.model.EmployeeEntity
import com.workfort.pstuian.featuredomain.model.TeacherEntity

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
        val batches: List<BatchEntity> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    data class TeacherListState(
        val teachers: List<TeacherEntity> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    data class CourseListState(
        val courses: List<CourseEntity> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    data class EmployeeListState(
        val employees: List<EmployeeEntity> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
    )
}
