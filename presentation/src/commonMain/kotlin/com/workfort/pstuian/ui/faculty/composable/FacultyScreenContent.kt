package com.workfort.pstuian.ui.faculty.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.Course
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.faculty.state.FacultyUiEvent
import com.workfort.pstuian.ui.faculty.state.FacultyUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FacultyScreenContent(
    uiState: FacultyUiState,
    onUiEvent: (FacultyUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = uiState.title,
                navigation = {
                    NavigationButton { onUiEvent(FacultyUiEvent.BackClicked) }
                },
            )
        },
    ) {
        when (uiState) {
            is FacultyUiState.None -> Unit
            is FacultyUiState.Content -> FacultyContentPanel(uiState, onUiEvent)
        }

        if (uiState.showOperationLoading) {
            FacultyScreenShimmer()
        }
    }
}

private fun mockTeachers() = listOf(
    User.Teacher(
        name = "Dr. Farhan Ahmed",
        email = "farhan.ahmed@pstu.ac.bd",
        facultyId = 1,
        phone = "+8801711000001",
        address = "Patuakhali",
        bio = null,
        blood = "A+",
        imageUrl = null,
        userId = 11,
        designation = "Professor",
        linkedIn = null,
        fbLink = null,
        department = "CSE",
        description = "Data science researcher",
    ),
)

private fun mockCourses() = listOf(
    Course(
        id = 101,
        courseCode = "CSE-311",
        courseTitle = "Operating Systems",
        creditHour = "3.0",
        facultyId = 1,
        status = 1,
    ),
)

private fun mockEmployees() = listOf(
    User.Employee(
        name = "Mizanur Rahman",
        email = "mizanur@pstu.ac.bd",
        facultyId = 1,
        phone = "+8801711000002",
        address = "Patuakhali",
        bio = null,
        blood = "B+",
        imageUrl = null,
        userId = 7,
        designation = "Office Assistant",
        department = "Admin",
    ),
)

private fun mockUiState(
    selectedTab: Int = 0,
    showLoadingOverlay: Boolean = false,
    showTeacherShimmer: Boolean = false,
    showCourseShimmer: Boolean = false,
    showEmployeeShimmer: Boolean = false,
): FacultyUiState.Content {
    return FacultyUiState.Content(
        title = "Faculty of CSE",
        showOperationLoading = showLoadingOverlay,
        facultyId = 1,
        tabs = listOf("Batch", "Teacher", "Course", "Employee"),
        selectedTab = selectedTab,
        teacherListState = FacultyUiState.TeacherListState(
            isLoading = showTeacherShimmer,
            teachers = if (showTeacherShimmer) emptyList() else mockTeachers(),
            error = null,
        ),
        courseListState = FacultyUiState.CourseListState(
            isLoading = showCourseShimmer,
            courses = if (showCourseShimmer) emptyList() else mockCourses(),
            error = null,
        ),
        employeeListState = FacultyUiState.EmployeeListState(
            isLoading = showEmployeeShimmer,
            employees = if (showEmployeeShimmer) emptyList() else mockEmployees(),
            error = null,
        ),
    )
}

@Preview(showBackground = true, name = "Light - Batch Tab")
@Composable
fun FacultyScreenContentBatchPreview() {
    AppTheme {
        FacultyScreenContent(uiState = mockUiState(selectedTab = 0), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Light - Teacher Tab")
@Composable
fun FacultyScreenContentTeacherPreview() {
    AppTheme {
        FacultyScreenContent(uiState = mockUiState(selectedTab = 1), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Dark - Course Tab")
@Composable
fun FacultyScreenContentDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        FacultyScreenContent(uiState = mockUiState(selectedTab = 2), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Light - Employee Tab")
@Composable
fun FacultyScreenContentEmployeePreview() {
    AppTheme {
        FacultyScreenContent(uiState = mockUiState(selectedTab = 3), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Overlay Loading")
@Composable
fun FacultyScreenContentLoadingOverlayPreview() {
    AppTheme {
        FacultyScreenContent(
            uiState = mockUiState(showLoadingOverlay = true),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Initial None")
@Composable
fun FacultyScreenContentNonePreview() {
    AppTheme {
        FacultyScreenContent(
            uiState = FacultyUiState.None(showOperationLoading = true),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Screen Shimmer")
@Composable
fun FacultyScreenContentScreenShimmerPreview() {
    AppTheme {
        FacultyScreenContent(
            uiState = FacultyUiState.None(),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Teacher List Shimmer")
@Composable
fun FacultyScreenContentTeacherShimmerPreview() {
    AppTheme {
        FacultyScreenContent(
            uiState = mockUiState(selectedTab = 1, showTeacherShimmer = true),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Course List Shimmer")
@Composable
fun FacultyScreenContentCourseShimmerPreview() {
    AppTheme {
        FacultyScreenContent(
            uiState = mockUiState(selectedTab = 2, showCourseShimmer = true),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Employee List Shimmer")
@Composable
fun FacultyScreenContentEmployeeShimmerPreview() {
    AppTheme {
        FacultyScreenContent(
            uiState = mockUiState(selectedTab = 3, showEmployeeShimmer = true),
            onUiEvent = {},
        )
    }
}
