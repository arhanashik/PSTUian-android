package com.workfort.pstuian.ui.blooddonation.blooddonationcreate

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.DatePickerDialog
import com.workfort.pstuian.ui.common.composable.HandleSnackbar
import com.workfort.pstuian.ui.common.composable.LoadingOverlay
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.blooddonation.blooddonationcreate.composable.BloodDonationCreateContentPanel
import com.workfort.pstuian.ui.blooddonation.blooddonationcreate.state.BloodDonationCreateMessageState
import com.workfort.pstuian.ui.blooddonation.blooddonationcreate.state.BloodDonationCreateNavigationState
import com.workfort.pstuian.ui.blooddonation.blooddonationcreate.state.BloodDonationCreateUiEvent
import com.workfort.pstuian.ui.blooddonation.blooddonationcreate.state.BloodDonationCreateUiState
import org.koin.compose.koinInject

@Composable
fun BloodDonationCreateScreen(viewModel: BloodDonationCreateViewModel) {
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
    uiState: BloodDonationCreateUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (BloodDonationCreateUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = "Create donation",
                navigation = {
                    NavigationButton { onUiEvent(BloodDonationCreateUiEvent.BackClicked) }
                },
            )
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) {
        when (uiState) {
            is BloodDonationCreateUiState.None -> Unit
            is BloodDonationCreateUiState.Content -> {
                BloodDonationCreateContentPanel(uiState = uiState, onUiEvent = onUiEvent)

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
    message: BloodDonationCreateMessageState?,
    snackbarHostState: SnackbarHostState,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is BloodDonationCreateMessageState.SelectDate -> {
                val selectableDates = object : SelectableDates { // only allow dates until today
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return utcTimeMillis <= it.allowedDateTill
                    }
                }
                DatePickerDialog(
                    selectableDates = selectableDates,
                    onDismissRequest = onMessageHandled,
                    onSelect = { dateMills -> it.onSelect(dateMills) },
                )
            }
            is BloodDonationCreateMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = { onMessageHandled() },
                    onDismiss = { onMessageHandled() },
                )
            }
            is BloodDonationCreateMessageState.Snackbar -> {
                HandleSnackbar(it.message, snackbarHostState, onMessageHandled)
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: BloodDonationCreateNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                BloodDonationCreateNavigationState.GoBack -> {
                    navigator?.goBack()
                }
            }
            onNavigationHandled()
        }
    }
}
