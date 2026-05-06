package com.workfort.pstuian.ui.donation.donors

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.common.composable.dialog.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowInfoDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.donation.donors.composable.DonorsScreenContent
import com.workfort.pstuian.ui.donation.donors.state.DonorsMessageState
import com.workfort.pstuian.ui.donation.donors.state.DonorsNavigationState
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorsScreen(viewModel: DonorsViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    DonorsScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: DonorsMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is DonorsMessageState.ShowDonorDetails -> {
                val item = it.donation
                val message = "Email: ${item.email}\n${item.message}\nReference: ${item.reference}"
                ShowInfoDialog(
                    title = item.name ?: "Donation Info",
                    message = message,
                    onDismiss = onMessageHandled,
                )
            }
            is DonorsMessageState.Error -> {
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
    navigation: DonorsNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                DonorsNavigationState.GoBack -> navigator?.goBack()
                DonorsNavigationState.DonateScreen -> navigator?.navigateTo(AppScreen.Donate)
            }
            onNavigationHandled()
        }
    }
}
