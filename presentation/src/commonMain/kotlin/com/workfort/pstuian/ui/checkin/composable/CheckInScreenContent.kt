package com.workfort.pstuian.ui.checkin.composable

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
import com.workfort.pstuian.ui.checkin.displaydata.CheckInDisplayData
import com.workfort.pstuian.ui.checkin.state.CheckInUiEvent
import com.workfort.pstuian.ui.checkin.state.CheckInUiState
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import pstuian.presentation.generated.resources.Res
import pstuian.presentation.generated.resources.label_check_in_screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CheckInScreenContent(
    uiState: CheckInUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (CheckInUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = stringResource(Res.string.label_check_in_screen),
                navigation = {
                    NavigationButton { onUiEvent(CheckInUiEvent.BackClicked) }
                },
            )
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
    ) {
        when (uiState) {
            is CheckInUiState.None -> Unit
            is CheckInUiState.Loading -> {
                CheckInFullScreenShimmer(modifier = Modifier.fillMaxSize())
            }
            is CheckInUiState.Content -> {
                CheckInContentPanel(
                    modifier = Modifier.fillMaxSize(),
                    uiState = uiState,
                    onUiEvent = onUiEvent,
                )
            }
            is CheckInUiState.Error -> {
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
    isCheckInLoading: Boolean = false,
): CheckInUiState.Content = CheckInUiState.Content(
    checkInLocations = mockCheckInLocations(),
    selectedLocationId = 1,
    currentUserCheckIn = currentUserCheckIn,
    otherCheckIns = otherCheckIns,
    isLocationListLoading = false,
    isCheckInLoading = isCheckInLoading,
)

@Preview(showBackground = true, name = "Loading")
@Composable
private fun CheckInScreenContentLoadingPreview() {
    AppTheme {
        CheckInScreenContent(
            uiState = CheckInUiState.Loading,
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Content")
@Composable
private fun CheckInScreenContentGridPreview() {
    AppTheme {
        CheckInScreenContent(
            uiState = mockCheckInContentUiState(),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Content - Dark")
@Composable
private fun CheckInScreenContentGridDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        CheckInScreenContent(
            uiState = mockCheckInContentUiState(),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Content - Check in here")
@Composable
private fun CheckInScreenContentPromptCardPreview() {
    AppTheme {
        CheckInScreenContent(
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
private fun CheckInScreenContentLoadMorePreview() {
    AppTheme {
        CheckInScreenContent(
            uiState = mockCheckInContentUiState(isCheckInLoading = true),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun CheckInScreenContentErrorPreview() {
    AppTheme {
        CheckInScreenContent(
            uiState = CheckInUiState.Error(error = null),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}
