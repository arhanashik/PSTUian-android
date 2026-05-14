package com.workfort.pstuian.ui.faculty.course.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.Course
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.faculty.course.state.CourseUiEvent
import com.workfort.pstuian.ui.faculty.course.state.CourseUiState

@Composable
internal fun CourseScreenContent(
    uiState: CourseUiState,
    onUiEvent: (CourseUiEvent) -> Unit,
) {
    when (uiState) {
        is CourseUiState.None -> Unit
        is CourseUiState.Content -> CourseContentPanel(uiState, onUiEvent)
        is CourseUiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedErrorView(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

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

@Preview(showBackground = true, name = "Course - Content")
@Composable
private fun CourseScreenContentPreview() {
    AppTheme {
        CourseScreenContent(
            uiState = CourseUiState.Content(courses = mockCourses()),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Course - Dark Content")
@Composable
private fun CourseScreenContentDarkPreview() {
    AppTheme(themeMode = ThemeMode.Dark) {
        CourseScreenContent(
            uiState = CourseUiState.Content(courses = mockCourses()),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Course - Loading")
@Composable
private fun CourseScreenContentLoadingPreview() {
    AppTheme {
        CourseScreenContent(
            uiState = CourseUiState.Content(isLoading = true),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Course - Empty")
@Composable
private fun CourseScreenContentEmptyPreview() {
    AppTheme {
        CourseScreenContent(
            uiState = CourseUiState.Content(),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Course - Error")
@Composable
private fun CourseScreenContentErrorPreview() {
    AppTheme {
        CourseScreenContent(
            uiState = CourseUiState.Error("Failed to load courses"),
            onUiEvent = {},
        )
    }
}
