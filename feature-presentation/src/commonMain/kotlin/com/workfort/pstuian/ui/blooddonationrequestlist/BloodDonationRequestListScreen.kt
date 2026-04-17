package com.workfort.pstuian.ui.blooddonationrequestlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.workfort.pstuian.ui.blooddonationrequestlist.composable.BloodDonationRequestListContentPanel
import com.workfort.pstuian.ui.blooddonationrequestlist.state.BloodDonationRequestListMessageState
import com.workfort.pstuian.ui.blooddonationrequestlist.state.BloodDonationRequestListNavigationState
import com.workfort.pstuian.ui.blooddonationrequestlist.state.BloodDonationRequestListUiEvent
import com.workfort.pstuian.ui.blooddonationrequestlist.state.BloodDonationRequestListUiState
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppBarIconButton
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowInfoDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_blood_donation_request_list_screen
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_msg_call
import pstuian.feature_presentation.generated.resources.txt_request_donation
import pstuian.feature_presentation.generated.resources.txt_title_call

@Composable
fun BloodDonationRequestListScreen(viewModel: BloodDonationRequestListViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    ScreenContent(uiState, snackbarHostState, onUiEvent = viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled, viewModel::onUiEvent)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
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
                actions = {
                    AppBarIconButton(
                        icon = Icons.Filled.Refresh,
                        onClick = {
                            onUiEvent(BloodDonationRequestListUiEvent.LoadMore(refresh = true))
                        },
                    )
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

@Composable
private fun HandleMessageState(
    message: BloodDonationRequestListMessageState?,
    onMessageHandled: () -> Unit,
    onUiEvent: (BloodDonationRequestListUiEvent) -> Unit,
) {
    message?.let {
        when (it) {
            is BloodDonationRequestListMessageState.ShowDetails -> {
                val date = it.item.beforeDate.split(" ")[0]
                ShowInfoDialog(
                    title = "Need ${it.item.bloodGroup} blood before $date",
                    message = it.item.info.orEmpty(),
                    onDismiss = onMessageHandled
                )
            }
            is BloodDonationRequestListMessageState.Call -> {
                ShowConfirmationDialog(
                    icon = Icons.Default.Call,
                    title = stringResource(Res.string.txt_title_call),
                    message = stringResource(Res.string.txt_msg_call).plus(" ${it.phoneNumber}"),
                    confirmButtonText = stringResource(Res.string.txt_call),
                    onConfirm = {
                        // TODO: Implement call functionality
                        onMessageHandled()
                    },
                    onDismiss = onMessageHandled
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: BloodDonationRequestListNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                BloodDonationRequestListNavigationState.GoBack -> {
                    navigator?.goBack()
                }
                BloodDonationRequestListNavigationState.BloodDonationRequestCreateScreen -> {
                    navigator?.navigateTo(AppScreen.BloodDonationRequestCreate)
                }
            }
            onNavigationHandled()
        }
    }
}
