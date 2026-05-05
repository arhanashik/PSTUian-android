package com.workfort.pstuian.ui.blooddonation.blooddonationinput

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.composable.BloodDonationInputScreenContent
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.state.BloodDonationInputMessageState
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.state.BloodDonationInputNavigationState
import com.workfort.pstuian.ui.common.composable.DatePickerDialog
import com.workfort.pstuian.ui.common.composable.HandleSnackbar
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import org.koin.compose.koinInject

@Composable
fun BloodDonationInputScreen(viewModel: BloodDonationInputViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    BloodDonationInputScreenContent(uiState, snackbarHostState, onUiEvent = viewModel::onUiEvent)

    HandleMessageState(message, snackbarHostState, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HandleMessageState(
    message: BloodDonationInputMessageState?,
    snackbarHostState: SnackbarHostState,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is BloodDonationInputMessageState.SelectDate -> {
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
            is BloodDonationInputMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = { onMessageHandled() },
                    onDismiss = { onMessageHandled() },
                )
            }
            is BloodDonationInputMessageState.Snackbar -> {
                HandleSnackbar(it.message, snackbarHostState, onMessageHandled)
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: BloodDonationInputNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                BloodDonationInputNavigationState.GoBack -> {
                    navigator?.goBack()
                }
            }
            onNavigationHandled()
        }
    }
}
