package com.workfort.pstuian.ui.faculty.faculty.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.faculty.faculty.state.FacultyUiEvent
import com.workfort.pstuian.ui.faculty.faculty.state.FacultyUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FacultyScreenContent(
    uiState: FacultyUiState,
    onUiEvent: (FacultyUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = (uiState as? FacultyUiState.Content)?.title,
                navigation = {
                    NavigationButton { onUiEvent(FacultyUiEvent.BackClicked) }
                },
            )
        },
    ) {
        when (uiState) {
            is FacultyUiState.None -> Unit
            is FacultyUiState.Loading -> FacultyScreenShimmer()
            is FacultyUiState.Content -> FacultyContentPanel(uiState, onUiEvent)
        }
    }
}

private fun mockUiState(selectedTab: Int = 0): FacultyUiState.Content {
    return FacultyUiState.Content(
        title = "Faculty of CSE",
        facultyId = 1,
        tabs = listOf("Batch", "Teacher", "Course", "Employee"),
        selectedTab = selectedTab,
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

@Preview(showBackground = true, name = "Initial None")
@Composable
fun FacultyScreenContentNonePreview() {
    AppTheme {
        FacultyScreenContent(
            uiState = FacultyUiState.None,
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Screen Shimmer")
@Composable
fun FacultyScreenContentScreenShimmerPreview() {
    AppTheme {
        FacultyScreenContent(
            uiState = FacultyUiState.Loading,
            onUiEvent = {},
        )
    }
}
