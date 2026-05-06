package com.workfort.pstuian.ui.donation.donationhistory.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.donation.donationhistory.state.DonationHistoryUiEvent
import com.workfort.pstuian.ui.donation.donationhistory.state.DonationHistoryUiState
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_donation_list
import pstuian.feature_presentation.generated.resources.txt_donate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DonationHistoryScreenContent(
    uiState: DonationHistoryUiState,
    onUiEvent: (DonationHistoryUiEvent) -> Unit,
) {
    var fabButtonExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = null) {
        delay(1000)
        fabButtonExpanded = false
    }

    AppScaffold (
        topBar = {
            AppBar(
                title = stringResource(Res.string.label_donation_list),
                navigation = {
                    NavigationButton { onUiEvent(DonationHistoryUiEvent.BackClicked) }
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
                    text = { Text(text = stringResource(Res.string.txt_donate)) },
                    onClick = { onUiEvent(DonationHistoryUiEvent.DonateClicked) },
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
    ) {
        when (uiState) {
            DonationHistoryUiState.None -> Unit
            is DonationHistoryUiState.Content -> {
                DonationHistoryContentPanel(
                    uiState = uiState,
                    onUiEvent = onUiEvent,
                )
            }
        }
    }
}