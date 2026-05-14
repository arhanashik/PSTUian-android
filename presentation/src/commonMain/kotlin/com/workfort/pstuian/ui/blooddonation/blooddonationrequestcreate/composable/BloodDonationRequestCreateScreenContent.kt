package com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.BloodDonationRequestInput
import com.workfort.pstuian.featuredomain.model.BloodDonationRequestInputError
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.state.BloodDonationRequestCreateUiEvent
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.state.BloodDonationRequestCreateUiState
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.LoadingOverlay
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import pstuian.presentation.generated.resources.Res
import pstuian.presentation.generated.resources.label_create_blood_donation_request_screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BloodDonationRequestCreateScreenContent(
    uiState: BloodDonationRequestCreateUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (BloodDonationRequestCreateUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = stringResource(Res.string.label_create_blood_donation_request_screen),
                navigation = {
                    NavigationButton { onUiEvent(BloodDonationRequestCreateUiEvent.BackClicked) }
                },
            )
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
    ) {
        when (uiState) {
            is BloodDonationRequestCreateUiState.None -> Unit
            is BloodDonationRequestCreateUiState.Content -> {
                BloodDonationRequestCreateContentPanel(uiState = uiState, onUiEvent = onUiEvent)

                if (uiState.isOperationLoading) {
                    LoadingOverlay()
                }
            }
        }
    }
}

@Preview
@Composable
private fun BloodDonationRequestCreateScreenContentPreview() {
    AppTheme {
        BloodDonationRequestCreateScreenContent(
            uiState = BloodDonationRequestCreateUiState.Content(
                input = BloodDonationRequestInput(
                    bloodGroup = "O+",
                    date = "2026-05-10",
                    contact = "01700000000",
                    message = "Urgent need at hospital ward 3. Any donor nearby would be greatly appreciated.",
                ),
                validationError = BloodDonationRequestInputError.INITIAL,
                isOperationLoading = false,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview
@Composable
private fun BloodDonationRequestCreateScreenContentDarkPreview() {
    AppTheme(themeMode = ThemeMode.Dark) {
        BloodDonationRequestCreateScreenContent(
            uiState = BloodDonationRequestCreateUiState.Content(
                input = BloodDonationRequestInput.INITIAL,
                validationError = BloodDonationRequestInputError(
                    bloodGroup = "Required",
                    date = "Pick a date",
                    contact = "",
                    message = "",
                ),
                isOperationLoading = false,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview
@Composable
private fun BloodDonationRequestCreateScreenContentLoadingPreview() {
    AppTheme {
        BloodDonationRequestCreateScreenContent(
            uiState = BloodDonationRequestCreateUiState.Content(
                input = BloodDonationRequestInput(
                    bloodGroup = "A+",
                    date = "2026-05-04",
                    contact = "01800000000",
                    message = "",
                ),
                validationError = BloodDonationRequestInputError.INITIAL,
                isOperationLoading = true,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}