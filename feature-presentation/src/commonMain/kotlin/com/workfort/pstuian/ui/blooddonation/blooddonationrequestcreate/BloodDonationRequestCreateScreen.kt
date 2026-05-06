package com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.composable.BloodDonationRequestCreateScreenContent
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.state.BloodDonationRequestCreateMessageState
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.state.BloodDonationRequestCreateNavigationState
import com.workfort.pstuian.ui.common.composable.dialog.DatePickerDialog
import com.workfort.pstuian.ui.common.composable.HandleSnackbar
import com.workfort.pstuian.ui.common.composable.dialog.ShowErrorDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import org.koin.compose.koinInject

@Composable
fun BloodDonationRequestCreateScreen(viewModel: BloodDonationRequestCreateViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    BloodDonationRequestCreateScreenContent(uiState, snackbarHostState, onUiEvent = viewModel::onUiEvent)

    HandleMessageState(message, snackbarHostState, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
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
                    onSelect = { dateMills ->
                        onMessageHandled()
                        it.onSelect(dateMills)
                    },
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
