package com.workfort.pstuian.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowInfoDialog
import com.workfort.pstuian.ui.common.composable.UserTypeSelectionBottomSheet
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.settings.composable.SettingsScreenContent
import com.workfort.pstuian.ui.settings.state.SettingsMessageState
import com.workfort.pstuian.ui.settings.state.SettingsNavigationState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_retry

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    SettingsScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: SettingsMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is SettingsMessageState.Error -> {
                ShowInfoDialog(
                    message = it.message,
                    dismissButtonText = stringResource(Res.string.txt_retry),
                    onDismiss = { onMessageHandled() }
                )
            }
            is SettingsMessageState.ConfirmClearPrefs -> {
                ShowConfirmationDialog(
                    title = it.title,
                    message = it.message,
                    onConfirm = it.onConfirm,
                    onDismiss = onMessageHandled,
                )
            }
            is SettingsMessageState.UserTypeSelection -> {
                UserTypeSelectionBottomSheet(
                    selectedUserType = it.selectedUserType,
                    onSaveAndContinue = { userType -> it.onSaveAndContinue(userType) },
                    onDismiss = onMessageHandled,
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: SettingsNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is SettingsNavigationState.GoBack -> navigator?.goBack()
                is SettingsNavigationState.GoToContactUs -> navigator?.navigateTo(AppScreen.ContactUs)
            }
            onNavigationHandled()
        }
    }
}
