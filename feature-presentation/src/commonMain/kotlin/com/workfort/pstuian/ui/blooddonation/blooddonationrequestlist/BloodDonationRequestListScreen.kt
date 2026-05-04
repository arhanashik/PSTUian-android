package com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.composable.BloodDonationRequestScreenContent
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state.BloodDonationRequestListMessageState
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state.BloodDonationRequestListNavigationState
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state.BloodDonationRequestListUiEvent
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowInfoDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_msg_call
import pstuian.feature_presentation.generated.resources.txt_title_call

@Composable
fun BloodDonationRequestListScreen(viewModel: BloodDonationRequestListViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    BloodDonationRequestScreenContent(uiState, snackbarHostState, onUiEvent = viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled, viewModel::onUiEvent)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
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
