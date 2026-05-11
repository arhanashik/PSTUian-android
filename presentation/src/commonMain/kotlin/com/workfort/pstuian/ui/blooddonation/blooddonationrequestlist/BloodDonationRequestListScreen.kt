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
import com.workfort.pstuian.ui.common.composable.HandleSnackbar
import com.workfort.pstuian.ui.common.composable.dialog.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowInfoDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.presentation.generated.resources.Res
import pstuian.presentation.generated.resources.blood_donation_request_status_active
import pstuian.presentation.generated.resources.blood_donation_request_status_approved
import pstuian.presentation.generated.resources.blood_donation_request_status_complete
import pstuian.presentation.generated.resources.blood_donation_request_status_pending
import pstuian.presentation.generated.resources.txt_call
import pstuian.presentation.generated.resources.txt_msg_call
import pstuian.presentation.generated.resources.txt_title_call

@Composable
fun BloodDonationRequestListScreen(viewModel: BloodDonationRequestListViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    BloodDonationRequestScreenContent(uiState, snackbarHostState, onUiEvent = viewModel::onUiEvent)

    HandleMessageState(message, snackbarHostState, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: BloodDonationRequestListMessageState?,
    snackbarHostState: SnackbarHostState,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is BloodDonationRequestListMessageState.ShowDetails -> {
                val date = it.item.beforeDate.split(" ")[0]
                val approvalLine = stringResource(
                    if (it.item.confirmed) {
                        Res.string.blood_donation_request_status_approved
                    } else {
                        Res.string.blood_donation_request_status_pending
                    },
                )
                val completionLine = stringResource(
                    if (it.item.completed) {
                        Res.string.blood_donation_request_status_complete
                    } else {
                        Res.string.blood_donation_request_status_active
                    },
                )
                val statusSummary = "$approvalLine · $completionLine"
                val detailBody = it.item.info.orEmpty()
                val dialogBody = listOf(statusSummary, detailBody).filter { segment -> segment.isNotBlank() }
                    .joinToString("\n\n")
                ShowInfoDialog(
                    title = "Need ${it.item.bloodGroup} blood before $date",
                    message = dialogBody,
                    onDismiss = onMessageHandled,
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
            is BloodDonationRequestListMessageState.Confirm -> {
                ShowConfirmationDialog(
                    message = it.message,
                    onConfirm = {
                        onMessageHandled()
                        it.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is BloodDonationRequestListMessageState.Snackbar -> {
                HandleSnackbar(it.message, snackbarHostState, onMessageHandled)
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
                is BloodDonationRequestListNavigationState.BloodDonationCreateScreen -> {
                    navigator?.navigateToBloodDonationInput(
                        donationId = null,
                        requestId = it.requestId,
                        it.userId,
                        it.userType,
                    )
                }
            }
            onNavigationHandled()
        }
    }
}
