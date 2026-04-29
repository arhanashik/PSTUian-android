package com.workfort.pstuian.ui.checkinlist.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.checkinlist.displaydata.CheckInDisplayData
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiEvent
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiState
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_check_in_screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CheckInListScreenContent(
    uiState: CheckInListUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (CheckInListUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = stringResource(Res.string.label_check_in_screen),
                navigation = {
                    NavigationButton { onUiEvent(CheckInListUiEvent.BackClicked) }
                },
            )
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
    ) {
        when (uiState) {
            is CheckInListUiState.None -> Unit
            is CheckInListUiState.Loading -> {
                CheckInListFullScreenShimmer(modifier = Modifier.fillMaxSize())
            }
            is CheckInListUiState.Content -> {
                CheckInListContentPanel(
                    modifier = Modifier.fillMaxSize(),
                    uiState = uiState,
                    onUiEvent = onUiEvent,
                )
            }
            is CheckInListUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedErrorView()
                }
            }
        }
    }
}

private fun mockCheckIn(
    id: Int,
    name: String,
    batch: String,
    phone: String? = "+8801711000001",
): CheckIn = CheckIn(
    id = id,
    locationId = 1,
    locationName = "Central Library",
    locationImageUrl = null,
    count = 8,
    privacy = "public",
    userId = id,
    userType = "student",
    name = name,
    batch = batch,
    phone = phone,
    imageUrl = null,
    date = "2026-04-30",
)

private fun mockCheckInLocations(): List<CheckInLocation> = listOf(
    CheckInLocation(
        id = 1,
        name = "Central Library",
        details = null,
        imageUrl = null,
        link = null,
        verified = 1,
        count = 42,
        userId = 0,
        userType = null,
    ),
    CheckInLocation(
        id = 2,
        name = "Physics Lawn",
        details = null,
        imageUrl = null,
        link = null,
        verified = 1,
        count = 17,
        userId = 0,
        userType = null,
    ),
)

private fun mockCheckInContentUiState(
    currentUserCheckIn: CheckInDisplayData? = CheckInDisplayData(
        checkIn = mockCheckIn(id = 101, name = "You", batch = "14th Batch"),
        isOnline = true,
    ),
    otherCheckIns: List<CheckInDisplayData> = listOf(
        CheckInDisplayData(
            checkIn = mockCheckIn(id = 201, name = "Farhana Rahman", batch = "13th Batch"),
            isOnline = true,
        ),
        CheckInDisplayData(
            checkIn = mockCheckIn(id = 202, name = "Karim Hassan", batch = "15th Batch", phone = null),
            isOnline = false,
        ),
    ),
    isCheckInListLoading: Boolean = false,
): CheckInListUiState.Content = CheckInListUiState.Content(
    checkInLocations = mockCheckInLocations(),
    selectedLocationId = 1,
    currentUserCheckIn = currentUserCheckIn,
    otherCheckIns = otherCheckIns,
    isLocationListLoading = false,
    isCheckInListLoading = isCheckInListLoading,
)

@Preview(showBackground = true, name = "Loading")
@Composable
private fun CheckInListScreenContentLoadingPreview() {
    AppTheme {
        CheckInListScreenContent(
            uiState = CheckInListUiState.Loading,
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Content")
@Composable
private fun CheckInListScreenContentGridPreview() {
    AppTheme {
        CheckInListScreenContent(
            uiState = mockCheckInContentUiState(),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Content - Dark")
@Composable
private fun CheckInListScreenContentGridDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        CheckInListScreenContent(
            uiState = mockCheckInContentUiState(),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Content - Check in here")
@Composable
private fun CheckInListScreenContentPromptCardPreview() {
    AppTheme {
        CheckInListScreenContent(
            uiState = mockCheckInContentUiState(
                currentUserCheckIn = null,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Content - Load more shimmer")
@Composable
private fun CheckInListScreenContentLoadMorePreview() {
    AppTheme {
        CheckInListScreenContent(
            uiState = mockCheckInContentUiState(isCheckInListLoading = true),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun CheckInListScreenContentErrorPreview() {
    AppTheme {
        CheckInListScreenContent(
            uiState = CheckInListUiState.Error(error = null),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}
