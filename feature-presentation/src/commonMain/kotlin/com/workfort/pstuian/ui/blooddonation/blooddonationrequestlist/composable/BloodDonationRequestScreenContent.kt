package com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.BloodDonationRequest
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state.BloodDonationRequestListUiEvent
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state.BloodDonationRequestListUiState
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.theme.AppTheme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_blood_donation_request_list_screen
import pstuian.feature_presentation.generated.resources.txt_request_donation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BloodDonationRequestScreenContent(
    uiState: BloodDonationRequestListUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (BloodDonationRequestListUiEvent) -> Unit,
) {
    var fabButtonExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = null) {
        delay(1000)
        fabButtonExpanded = false
    }

    AppScaffold(
        topBar = {
            AppBar(
                title = stringResource(Res.string.label_blood_donation_request_list_screen),
                navigation = {
                    NavigationButton { onUiEvent(BloodDonationRequestListUiEvent.BackClicked) }
                },
            )
        },
        floatingActionButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                ExtendedFloatingActionButton(
                    expanded = fabButtonExpanded,
                    text = { Text(text = stringResource(Res.string.txt_request_donation)) },
                    onClick = {
                        onUiEvent(BloodDonationRequestListUiEvent.CreateRequestClicked)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "",
                        )
                    },
                    shape = CircleShape,
                )
            }
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
    ) {
        when (uiState) {
            is BloodDonationRequestListUiState.None -> Unit
            is BloodDonationRequestListUiState.Content -> {
                BloodDonationRequestListContentPanel(uiState = uiState, onUiEvent = onUiEvent)
            }
        }
    }
}

private fun mockBloodDonationRequest(
    id: Int = 1,
    name: String = "Jamil Ahmed",
    bloodGroup: String = "O+",
) = BloodDonationRequest(
    id = id,
    bloodGroup = bloodGroup,
    beforeDate = "2026-05-10 12:00:00",
    contacts = "01711111111,01822222222",
    info = "Urgent need at PSTU Medical Centre. Any donor nearby would be appreciated.",
    userId = "42",
    userType = "student",
    name = name,
    imageUrl = null,
    confirmed = true,
    completed = false,
)

private fun mockContent(
    requestList: List<BloodDonationRequest> = listOf(mockBloodDonationRequest()),
    isLoading: Boolean = false,
    error: String? = null,
) = BloodDonationRequestListUiState.Content(
    requestList = requestList,
    isLoading = isLoading,
    error = error,
)

@Preview(showBackground = true, name = "List")
@Composable
fun BloodDonationRequestScreenContentListPreview() {
    AppTheme {
        BloodDonationRequestScreenContent(
            uiState = mockContent(
                requestList = listOf(
                    mockBloodDonationRequest(id = 1, name = "Jamil Ahmed"),
                    mockBloodDonationRequest(id = 2, name = "Nusrat Jahan", bloodGroup = "B+"),
                ),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Loading more")
@Composable
fun BloodDonationRequestScreenContentLoadingMorePreview() {
    AppTheme {
        BloodDonationRequestScreenContent(
            uiState = mockContent(
                requestList = listOf(mockBloodDonationRequest()),
                isLoading = true,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Initial shimmer")
@Composable
fun BloodDonationRequestScreenContentShimmerPreview() {
    AppTheme {
        BloodDonationRequestScreenContent(
            uiState = mockContent(requestList = emptyList(), isLoading = true),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Empty")
@Composable
fun BloodDonationRequestScreenContentEmptyPreview() {
    AppTheme {
        BloodDonationRequestScreenContent(
            uiState = mockContent(requestList = emptyList(), isLoading = false),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Dark – list")
@Composable
fun BloodDonationRequestScreenContentDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        BloodDonationRequestScreenContent(
            uiState = mockContent(
                requestList = listOf(
                    mockBloodDonationRequest(id = 1),
                    mockBloodDonationRequest(id = 2, name = "Rafiq Hasan", bloodGroup = "A-"),
                ),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "None")
@Composable
fun BloodDonationRequestScreenContentNonePreview() {
    AppTheme {
        BloodDonationRequestScreenContent(
            uiState = BloodDonationRequestListUiState.None,
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}