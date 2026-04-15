package com.workfort.pstuian.ui.blooddonationrequestcreate

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.workfort.pstuian.common.composable.AppBar
import com.workfort.pstuian.common.composable.AppScaffold
import com.workfort.pstuian.common.composable.AppSnackbarHost
import com.workfort.pstuian.common.composable.DatePickerDialog
import com.workfort.pstuian.common.composable.HandleSnackbar
import com.workfort.pstuian.common.composable.LoadingOverlay
import com.workfort.pstuian.common.composable.NavigationButton
import com.workfort.pstuian.common.composable.ShowErrorDialog
import com.workfort.pstuian.common.navigation.AppNavigator
import com.workfort.pstuian.ui.blooddonationrequestcreate.composable.BloodDonationRequestCreateContentPanel
import com.workfort.pstuian.ui.blooddonationrequestcreate.state.BloodDonationRequestCreateMessageState
import com.workfort.pstuian.ui.blooddonationrequestcreate.state.BloodDonationRequestCreateNavigationState
import com.workfort.pstuian.ui.blooddonationrequestcreate.state.BloodDonationRequestCreateUiEvent
import com.workfort.pstuian.ui.blooddonationrequestcreate.state.BloodDonationRequestCreateUiState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_create_blood_donation_request_screen

@Composable
internal fun BloodDonationRequestCreateScreen(viewModel: BloodDonationRequestCreateViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    ScreenContent(uiState, snackbarHostState, onUiEvent = viewModel::onUiEvent)

    HandleMessageState(message, snackbarHostState, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
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
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HandleMessageState(
    message: BloodDonationRequestCreateMessageState?,
    snackbarHostState: SnackbarHostState,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is BloodDonationRequestCreateMessageState.SelectDate -> {
                val selectableDates = object : SelectableDates { // only allow dates from today
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return utcTimeMillis >= it.allowedDateFrom
                    }
                }
                DatePickerDialog(
                    selectableDates = selectableDates,
                    onDismissRequest = onMessageHandled,
                    onSelect = { dateMills -> it.onSelect(dateMills) },
                )
            }
            is BloodDonationRequestCreateMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = { onMessageHandled() },
                    onDismiss = { onMessageHandled() },
                )
            }
            is BloodDonationRequestCreateMessageState.Snackbar -> {
                HandleSnackbar(it.message, snackbarHostState, onMessageHandled)
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: BloodDonationRequestCreateNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                BloodDonationRequestCreateNavigationState.GoBack -> {
                    navigator?.goBack()
                }
            }
            onNavigationHandled()
        }
    }
}
