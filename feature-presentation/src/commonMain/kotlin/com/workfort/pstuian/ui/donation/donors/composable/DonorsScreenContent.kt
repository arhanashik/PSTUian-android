package com.workfort.pstuian.ui.donation.donors.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppBarIconButton
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.donation.donors.state.DonorsUiEvent
import com.workfort.pstuian.ui.donation.donors.state.DonorsUiState
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_donation_list
import pstuian.feature_presentation.generated.resources.txt_donate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DonorsScreenContent(
    uiState: DonorsUiState,
    onUiEvent: (DonorsUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
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
                    NavigationButton { onUiEvent(DonorsUiEvent.BackClicked) }
                },
                actions = {
                    AppBarIconButton(
                        icon = Icons.Filled.Refresh,
                        onClick = { onUiEvent(DonorsUiEvent.Refresh) },
                    )
                },
                scrollBehavior = scrollBehavior,
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
                    onClick = { onUiEvent(DonorsUiEvent.DonateClicked) },
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
            DonorsUiState.None -> Unit
            DonorsUiState.Loading -> {
                DonorsListShimmer()
            }
            is DonorsUiState.Content -> {
                DonorsContentPanel(
                    uiState = uiState,
                    onUiEvent = onUiEvent,
                )
            }
        }
    }
}