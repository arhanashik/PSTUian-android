package com.workfort.pstuian.ui.blooddonation.blooddonationhistory.composable

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
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.BloodDonationEntity
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state.BloodDonationHistoryUiEvent
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state.BloodDonationHistoryUiState
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.composable.dialog.ShowLoaderDialog
import com.workfort.pstuian.ui.common.theme.AppTheme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import pstuian.presentation.generated.resources.Res
import pstuian.presentation.generated.resources.txt_blood_donation_history
import pstuian.presentation.generated.resources.txt_create_new

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BloodDonationHistoryScreenContent(
    uiState: BloodDonationHistoryUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (BloodDonationHistoryUiEvent) -> Unit,
) {
    var fabButtonExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = null) {
        delay(1000)
        fabButtonExpanded = false
    }

    AppScaffold(
        topBar = {
            AppBar(
                title = stringResource(Res.string.txt_blood_donation_history),
                navigation = {
                    NavigationButton { onUiEvent(BloodDonationHistoryUiEvent.BackClicked) }
                },
            )
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                expanded = fabButtonExpanded,
                text = { Text(text = stringResource(Res.string.txt_create_new)) },
                onClick = {
                    onUiEvent(BloodDonationHistoryUiEvent.CreateDonationClicked)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "",
                    )
                },
                shape = CircleShape,
            )
        },
    ) {
        when (uiState) {
            BloodDonationHistoryUiState.None -> Unit
            is BloodDonationHistoryUiState.Content -> {
                BloodDonationHistoryContentPanel(uiState, onUiEvent)
                if (uiState.isOperationLoading) {
                    ShowLoaderDialog(cancelable = false)
                }
            }
        }
    }
}

private fun mockDonations() = listOf(
    BloodDonationEntity(
        id = 1,
        requestId = 101,
        date = "2026-05-05 10:30:00",
        info = "Donated at PSTU medical center.",
        userId = "1",
        userType = "student",
        name = "Preview User",
        imageUrl = null,
    ),
    BloodDonationEntity(
        id = 2,
        requestId = null,
        date = "2026-05-01 09:15:00",
        info = "Emergency donation for ward B.",
        userId = "1",
        userType = "student",
        name = "Preview User",
        imageUrl = null,
    ),
)

@Preview(showBackground = true, name = "Content")
@Composable
private fun BloodDonationHistoryScreenContentPreview() {
    AppTheme {
        BloodDonationHistoryScreenContent(
            uiState = BloodDonationHistoryUiState.Content(
                donations = mockDonations(),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Loading / Shimmer")
@Composable
private fun BloodDonationHistoryScreenContentLoadingPreview() {
    AppTheme {
        BloodDonationHistoryScreenContent(
            uiState = BloodDonationHistoryUiState.Content(
                donations = emptyList(),
                isContentLoading = true,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Dark - Content")
@Composable
private fun BloodDonationHistoryScreenContentDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        BloodDonationHistoryScreenContent(
            uiState = BloodDonationHistoryUiState.Content(
                donations = mockDonations(),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}