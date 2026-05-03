package com.workfort.pstuian.ui.mycheckinlist.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.CheckInPrivacy
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.LoadingOverlay
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInListUiEvent
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInListUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_my_check_in_list

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MyCheckInListScreenContent(
    uiState: MyCheckInListUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (MyCheckInListUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    AppScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = stringResource(Res.string.txt_my_check_in_list),
                navigation = {
                    NavigationButton { onUiEvent(MyCheckInListUiEvent.BackClicked) }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
    ) {
        when (uiState) {
            MyCheckInListUiState.None -> Unit
            is MyCheckInListUiState.Content -> {
                MyCheckInListContentPanel(uiState, onUiEvent)

                if (uiState.isOperationLoading) {
                    LoadingOverlay()
                }
            }
        }
    }
}

private fun mockMyCheckIns() = listOf(
    CheckIn(
        id = 1,
        locationId = 101,
        locationName = "Central Library",
        locationImageUrl = null,
        count = 42,
        privacy = CheckInPrivacy.PUBLIC.value,
        userId = 1,
        userType = UserType.STUDENT.type,
        name = "Preview User",
        batch = "2019-20",
        phone = null,
        imageUrl = null,
        date = "2025-01-15T10:30:00Z",
    ),
    CheckIn(
        id = 2,
        locationId = 102,
        locationName = "Faculty of Science & Engineering",
        locationImageUrl = null,
        count = 1280,
        privacy = CheckInPrivacy.ONLY_ME.value,
        userId = 1,
        userType = UserType.STUDENT.type,
        name = "Preview User",
        batch = "2019-20",
        phone = null,
        imageUrl = null,
        date = "2025-01-10T08:00:00Z",
    ),
)

@Preview(showBackground = true, name = "My check-ins – Content")
@Composable
private fun MyCheckInListScreenContentPreview() {
    AppTheme {
        MyCheckInListScreenContent(
            uiState = MyCheckInListUiState.Content(
                checkIns = mockMyCheckIns(),
            ),
            snackbarHostState = SnackbarHostState(),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "My check-ins – Dark")
@Composable
private fun MyCheckInListScreenContentDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        MyCheckInListScreenContent(
            uiState = MyCheckInListUiState.Content(
                checkIns = mockMyCheckIns(),
            ),
            snackbarHostState = SnackbarHostState(),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "My check-ins – Initial loading")
@Composable
private fun MyCheckInListScreenContentLoadingPreview() {
    AppTheme {
        MyCheckInListScreenContent(
            uiState = MyCheckInListUiState.Content(
                checkIns = emptyList(),
                isOperationLoading = true,
            ),
            snackbarHostState = SnackbarHostState(),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "My check-ins – Loading more")
@Composable
private fun MyCheckInListScreenContentLoadingMorePreview() {
    AppTheme(theme = ThemeMode.Dark) {
        MyCheckInListScreenContent(
            uiState = MyCheckInListUiState.Content(
                checkIns = mockMyCheckIns(),
                isContentLoading = true,
            ),
            snackbarHostState = SnackbarHostState(),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "My check-ins – Empty")
@Composable
private fun MyCheckInListScreenContentEmptyPreview() {
    AppTheme {
        MyCheckInListScreenContent(
            uiState = MyCheckInListUiState.Content(
                checkIns = emptyList(),
            ),
            snackbarHostState = SnackbarHostState(),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "My check-ins – Error")
@Composable
private fun MyCheckInListScreenContentErrorPreview() {
    AppTheme {
        MyCheckInListScreenContent(
            uiState = MyCheckInListUiState.Content(
                error = "Failed to load check-ins",
            ),
            snackbarHostState = SnackbarHostState(),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "My check-ins – Operation loading")
@Composable
private fun MyCheckInListScreenContentOperationLoadingPreview() {
    AppTheme {
        MyCheckInListScreenContent(
            uiState = MyCheckInListUiState.Content(
                isOperationLoading = true,
                checkIns = mockMyCheckIns(),
                isContentLoading = false,
            ),
            snackbarHostState = SnackbarHostState(),
            onUiEvent = {},
        )
    }
}