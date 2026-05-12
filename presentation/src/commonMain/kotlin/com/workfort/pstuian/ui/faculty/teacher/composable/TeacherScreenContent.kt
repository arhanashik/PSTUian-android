package com.workfort.pstuian.ui.faculty.teacher.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.faculty.teacher.state.TeacherUiEvent
import com.workfort.pstuian.ui.faculty.teacher.state.TeacherUiState

@Composable
internal fun TeacherScreenContent(
    uiState: TeacherUiState,
    onUiEvent: (TeacherUiEvent) -> Unit,
) {
    when (uiState) {
        is TeacherUiState.None -> Unit
        is TeacherUiState.Content -> TeacherContentPanel(uiState, onUiEvent)
        is TeacherUiState.Error -> {
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

@Preview(showBackground = true, name = "Teacher - Content")
@Composable
private fun TeacherScreenContentPreview() {
    AppTheme {
        TeacherScreenContent(
            uiState = TeacherUiState.Content(teachers = mockTeachers()),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Teacher - Dark Content")
@Composable
private fun TeacherScreenContentDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        TeacherScreenContent(
            uiState = TeacherUiState.Content(teachers = mockTeachers()),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Teacher - Loading")
@Composable
private fun TeacherScreenContentLoadingPreview() {
    AppTheme {
        TeacherScreenContent(
            uiState = TeacherUiState.Content(isLoading = true),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Teacher - Empty")
@Composable
private fun TeacherScreenContentEmptyPreview() {
    AppTheme {
        TeacherScreenContent(
            uiState = TeacherUiState.Content(),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Teacher - Error")
@Composable
private fun TeacherScreenContentErrorPreview() {
    AppTheme {
        TeacherScreenContent(
            uiState = TeacherUiState.Error("Failed to load teachers"),
            onUiEvent = {},
        )
    }
}
