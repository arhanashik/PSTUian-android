package com.workfort.pstuian.ui.donation.donationhistory

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.common.composable.dialog.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowInfoDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.donation.donationhistory.composable.DonationHistoryScreenContent
import com.workfort.pstuian.ui.donation.donationhistory.state.DonationHistoryMessageState
import com.workfort.pstuian.ui.donation.donationhistory.state.DonationHistoryNavigationState
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationHistoryScreen(viewModel: DonationHistoryViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    DonationHistoryScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: DonationHistoryMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is DonationHistoryMessageState.ShowDonorDetails -> {
                val item = it.donation
                val message = "Email: ${item.email}\n${item.message}\nReference: ${item.reference}"
                ShowInfoDialog(
                    title = item.name ?: "Donation Info",
                    message = message,
                    onDismiss = onMessageHandled,
                )
            }
            is DonationHistoryMessageState.Error -> {
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
    navigation: DonationHistoryNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                DonationHistoryNavigationState.GoBack -> navigator?.goBack()
                DonationHistoryNavigationState.DonateScreen -> navigator?.navigateTo(AppScreen.Donate)
            }
            onNavigationHandled()
        }
    }
}
