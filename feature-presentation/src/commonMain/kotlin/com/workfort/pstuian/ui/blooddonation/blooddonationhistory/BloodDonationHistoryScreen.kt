package com.workfort.pstuian.ui.blooddonation.blooddonationhistory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.composable.BloodDonationHistoryScreenContent
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state.BloodDonationHistoryMessageState
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state.BloodDonationHistoryNavigationState
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowInfoDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.msg_delete_permanent
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
