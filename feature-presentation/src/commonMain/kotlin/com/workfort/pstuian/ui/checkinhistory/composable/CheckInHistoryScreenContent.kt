package com.workfort.pstuian.ui.checkinhistory.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.CheckInPrivacy
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.LoadingOverlay
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.checkinhistory.state.CheckInHistoryUiEvent
import com.workfort.pstuian.ui.checkinhistory.state.CheckInHistoryUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_check_in_history

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CheckInHistoryScreenContent(
    uiState: CheckInHistoryUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (CheckInHistoryUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = stringResource(Res.string.txt_check_in_history),
                navigation = {
                    NavigationButton { onUiEvent(CheckInHistoryUiEvent.BackClicked) }
                },
            )
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
    ) {
        when (uiState) {
            CheckInHistoryUiState.None -> Unit
            is CheckInHistoryUiState.Content -> {
                CheckInHistoryContentPanel(uiState, onUiEvent)

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
private fun CheckInHistoryScreenContentPreview() {
    AppTheme {
        CheckInHistoryScreenContent(
            uiState = CheckInHistoryUiState.Content(
                checkIns = mockMyCheckIns(),
            ),
            snackbarHostState = SnackbarHostState(),
            onUiEvent = {},
        )
    }
}
