package com.workfort.pstuian.ui.donors

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppBarIconButton
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.LoadingOverlay
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.ShowInfoDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.donors.composable.DonorsContentPanel
import com.workfort.pstuian.ui.donors.state.DonorsMessageState
import com.workfort.pstuian.ui.donors.state.DonorsNavigationState
import com.workfort.pstuian.ui.donors.state.DonorsUiEvent
import com.workfort.pstuian.ui.donors.state.DonorsUiState
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_donation_list
import pstuian.feature_presentation.generated.resources.txt_donate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorsScreen(viewModel: DonorsViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    DonorsScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DonorsScreenContent(
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
                    onUiEvent(DonorsUiEvent.BackClicked)
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
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End,
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
                LoadingOverlay()
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

@Composable
private fun HandleMessageState(
    message: DonorsMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is DonorsMessageState.ShowDonorDetails -> {
                val item = it.donor
                val message = "Email: ${item.email}\n${item.info}\nReference: ${item.reference}"
                ShowInfoDialog(
                    title = item.name ?: "Donation Info",
                    message = message,
                    onDismiss = onMessageHandled,
                )
            }
            is DonorsMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = { onMessageHandled() },
                    onDismiss = { onMessageHandled() },
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: DonorsNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                DonorsNavigationState.GoBack -> navigator?.goBack()
                DonorsNavigationState.DonateScreen -> navigator?.navigateTo(AppScreen.Donate)
            }
            onNavigationHandled()
        }
    }
}
