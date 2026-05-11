package com.workfort.pstuian.ui.donation.donate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.common.composable.dialog.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowSuccessDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.donation.donate.composable.DonateScreenContent
import com.workfort.pstuian.ui.donation.donate.state.DonateMessageState
import com.workfort.pstuian.ui.donation.donate.state.DonateNavigationState
import org.koin.compose.koinInject

@Composable
fun DonateScreen(viewModel: DonateViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    DonateScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: DonateMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is DonateMessageState.Success -> {
                ShowSuccessDialog(
                    cancelable = it.cancelable,
                    message = it.message,
                    onConfirm = {
                        onMessageHandled()
                        it.onConfirm()
                    },
                )
            }
            is DonateMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: DonateNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                DonateNavigationState.GoBack -> navigator?.goBack()
            }
            onNavigationHandled()
        }
    }
}