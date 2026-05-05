package com.workfort.pstuian.ui.blooddonationhistory

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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppBarIconButton
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowInfoDialog
import com.workfort.pstuian.ui.common.composable.ShowLoaderDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.blooddonationhistory.composable.BloodDonationHistoryContentPanel
import com.workfort.pstuian.ui.blooddonationhistory.state.BloodDonationHistoryMessageState
import com.workfort.pstuian.ui.blooddonationhistory.state.BloodDonationHistoryNavigationState
import com.workfort.pstuian.ui.blooddonationhistory.state.BloodDonationHistoryUiEvent
import com.workfort.pstuian.ui.blooddonationhistory.state.BloodDonationHistoryUiState
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.msg_delete_permanent
import pstuian.feature_presentation.generated.resources.txt_blood_donation_history
import pstuian.feature_presentation.generated.resources.txt_create_new
import pstuian.feature_presentation.generated.resources.txt_retry

@Composable
fun BloodDonationHistoryScreen(viewModel: BloodDonationHistoryViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    BloodDonationHistoryScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BloodDonationHistoryScreenContent(
    uiState: BloodDonationHistoryUiState,
    onUiEvent: (BloodDonationHistoryUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var fabButtonExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = null) {
        delay(1000)
        fabButtonExpanded = false
    }

    AppScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = stringResource(Res.string.txt_blood_donation_history),
                navigation = {
                    NavigationButton { onUiEvent(BloodDonationHistoryUiEvent.BackClicked) }
                },
                actions = {
                    AppBarIconButton(
                        icon = Icons.Filled.Refresh,
                        onClick = {
                            onUiEvent(BloodDonationHistoryUiEvent.LoadList(refresh = true))
                        }
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                expanded = fabButtonExpanded,
                text = { Text(text = stringResource(Res.string.txt_create_new)) },
                onClick = {
                    onUiEvent(BloodDonationHistoryUiEvent.CreateRequestClicked)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "",
                    )
                },
            )
        },
    ) {
        BloodDonationHistoryContentPanel(uiState, onUiEvent)
    }
}

@Composable
private fun HandleMessageState(
    message: BloodDonationHistoryMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is BloodDonationHistoryMessageState.ConfirmDelete -> {
                ShowConfirmationDialog(
                    message = stringResource(Res.string.msg_delete_permanent),
                    onConfirm = {
                        onMessageHandled()
                        it.onConfirm()
                    },
                    onDismiss = { onMessageHandled() }
                )
            }
            is BloodDonationHistoryMessageState.Loading -> {
                ShowLoaderDialog(cancelable = it.cancelable)
            }
            is BloodDonationHistoryMessageState.Success -> {
                ShowInfoDialog(
                    message = it.message,
                    onDismiss = { onMessageHandled() }
                )
            }
            is BloodDonationHistoryMessageState.Error -> {
                ShowInfoDialog(
                    message = it.message,
                    dismissButtonText = stringResource(Res.string.txt_retry),
                    onDismiss = { onMessageHandled() }
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: BloodDonationHistoryNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is BloodDonationHistoryNavigationState.GoBack -> navigator?.goBack()
                is BloodDonationHistoryNavigationState.GoToCreateBloodDonationRequest -> {
                    navigator?.navigateTo(AppScreen.BloodDonationRequestCreate)
                }
                is BloodDonationHistoryNavigationState.GoToEditBloodDonationRequest -> {
                    navigator?.navigateTo(
                        AppScreen.BloodDonationRequestEdit(it.donationId),
                    )
                }
            }
            onNavigationHandled()
        }
    }
}
