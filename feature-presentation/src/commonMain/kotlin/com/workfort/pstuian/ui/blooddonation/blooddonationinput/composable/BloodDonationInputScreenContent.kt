package com.workfort.pstuian.ui.blooddonation.blooddonationinput.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.state.BloodDonationInputUiEvent
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.state.BloodDonationInputUiState
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.LoadingOverlay
import com.workfort.pstuian.ui.common.composable.NavigationButton

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