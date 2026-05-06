package com.workfort.pstuian.ui.donation.donate.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.LoadingOverlay
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.donation.donate.state.DonateUiEvent
import com.workfort.pstuian.ui.donation.donate.state.DonateUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_donate_screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DonateScreenContent(
    uiState: DonateUiState,
    onUiEvent: (DonateUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = stringResource(Res.string.label_donate_screen),
                navigation = {
                    NavigationButton {
                        onUiEvent(DonateUiEvent.BackClicked)
                    }
                },
            )
        },
    ) {
        Column(modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        ) {
            DonateContentPanel(uiState, onUiEvent)
        }
        if (uiState.isLoading) {
            LoadingOverlay()
        }
    }
}