package com.workfort.pstuian.ui.blooddonation.blooddonationinput.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.state.BloodDonationInputData
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.state.BloodDonationInputUiEvent
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.state.BloodDonationInputUiState
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.LoadingOverlay
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BloodDonationInputScreenContent(
    uiState: BloodDonationInputUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (BloodDonationInputUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = uiState.title,
                navigation = {
                    NavigationButton { onUiEvent(BloodDonationInputUiEvent.BackClicked) }
                },
            )
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) {
        when (uiState) {
            is BloodDonationInputUiState.None -> Unit
            is BloodDonationInputUiState.Content -> {
                BloodDonationInputContentPanel(uiState = uiState, onUiEvent = onUiEvent)

                if (uiState.isOperationLoading) {
                    LoadingOverlay()
                }
            }
        }
    }
}

@Preview
@Composable
private fun BloodDonationInputScreenContentPreview() {
    AppTheme {
        BloodDonationInputScreenContent(
            uiState = BloodDonationInputUiState.Content(
                title = "Blood Donation Input",
                inputData = BloodDonationInputData(
                    requestId = 1234,
                    formattedDate = "2026-05-10",
                    info = "Donated at central hospital blood bank.",
                ),
                enableSendButton = true,
                isOperationLoading = false,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview
@Composable
private fun BloodDonationInputScreenContentDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        BloodDonationInputScreenContent(
            uiState = BloodDonationInputUiState.Content(
                title = "Blood Donation Input",
                inputData = BloodDonationInputData(
                    requestId = 0,
                    formattedDate = "",
                    info = "",
                ),
                enableSendButton = false,
                isOperationLoading = false,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview
@Composable
private fun BloodDonationInputScreenContentLoadingPreview() {
    AppTheme {
        BloodDonationInputScreenContent(
            uiState = BloodDonationInputUiState.Content(
                title = "Blood Donation Input",
                inputData = BloodDonationInputData(
                    requestId = 4567,
                    formattedDate = "2026-05-06",
                    info = "Immediate response from donor received.",
                ),
                enableSendButton = true,
                isOperationLoading = true,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}