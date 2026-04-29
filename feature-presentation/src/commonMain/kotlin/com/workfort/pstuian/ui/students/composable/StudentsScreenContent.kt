package com.workfort.pstuian.ui.students.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.students.state.StudentsUiEvent
import com.workfort.pstuian.ui.students.state.StudentsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StudentsScreenContent(
    uiState: StudentsUiState,
    onUiEvent: (StudentsUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = (uiState as? StudentsUiState.Content)?.title,
                navigation = {
                    NavigationButton { onUiEvent(StudentsUiEvent.BackClicked) }
                },
            )
        },
    ) {
        when (uiState) {
            is StudentsUiState.None -> Unit
            is StudentsUiState.Loading -> StudentsListShimmer()
            is StudentsUiState.Content -> StudentsContentPanel(uiState, onUiEvent)
            is StudentsUiState.Error -> {
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
}

private fun mockStudents() = listOf(
    User.Student(
        userId = "st-1",
        id = 1201,
        name = "Nafis Rahman",
        email = "nafis.rahman@pstu.ac.bd",
        facultyId = 1,
        phone = "+8801710000001",
        address = "Patuakhali",
        bio = null,
        blood = "B+",
        imageUrl = null,
        reg = "2019-221-001",
        batchId = 14,
        session = "2019-20",
        linkedIn = null,
        fbLink = null,
        cvLink = null,
    ),
    User.Student(
        userId = "st-2",
        id = 1202,
        name = "Sadia Islam",
        email = "sadia.islam@pstu.ac.bd",
        facultyId = 1,
        phone = "+8801710000002",
        address = "Patuakhali",
        bio = null,
        blood = "A+",
        imageUrl = null,
        reg = "2019-221-002",
        batchId = 14,
        session = "2019-20",
        linkedIn = null,
        fbLink = null,
        cvLink = null,
    ),
)

@Preview(showBackground = true, name = "Students - Content")
@Composable
private fun StudentsScreenContentPreview() {
    AppTheme {
        StudentsScreenContent(
            uiState = StudentsUiState.Content(
                title = "Students",
                isLoading = false,
                students = mockStudents(),
            ),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Students - Dark Content")
@Composable
private fun StudentsScreenContentDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        StudentsScreenContent(
            uiState = StudentsUiState.Content(
                title = "Students",
                students = mockStudents(),
            ),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Students - Loading")
@Composable
private fun StudentsScreenContentLoadingPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        StudentsScreenContent(
            uiState = StudentsUiState.Content(
                title = "Students",
                students = emptyList(),
                isLoading = true,
            ),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Students - Loading More")
@Composable
private fun StudentsScreenContentLoadingMorePreview() {
    AppTheme(theme = ThemeMode.Dark) {
        StudentsScreenContent(
            uiState = StudentsUiState.Content(
                title = "Students",
                students = mockStudents(),
                isLoading = true,
            ),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Students - Empty")
@Composable
private fun StudentsScreenContentEmptyPreview() {
    AppTheme {
        StudentsScreenContent(
            uiState = StudentsUiState.Content(
                title = "Students",
                isLoading = false,
                students = emptyList(),
            ),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Students - Error")
@Composable
private fun StudentsScreenContentErrorPreview() {
    AppTheme {
        StudentsScreenContent(
            uiState = StudentsUiState.Error("Failed to load students"),
            onUiEvent = {},
        )
    }
}