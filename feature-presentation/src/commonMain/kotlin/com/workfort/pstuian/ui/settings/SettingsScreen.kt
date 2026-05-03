package com.workfort.pstuian.ui.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowInfoDialog
import com.workfort.pstuian.ui.common.composable.ListSelectionBottomSheet
import com.workfort.pstuian.ui.common.composable.userTypeListSelectionOptions
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.settings.composable.SettingsScreenContent
import com.workfort.pstuian.ui.settings.state.SettingsMessageState
import com.workfort.pstuian.ui.settings.state.SettingsNavigationState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.btn_save_and_continue
import pstuian.feature_presentation.generated.resources.helper_app_usage_role_sheet
import pstuian.feature_presentation.generated.resources.title_select_app_usage_role
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

@OptIn(ExperimentalMaterial3Api::class)
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
            is SettingsMessageState.ConfirmAction -> {
                ShowConfirmationDialog(
                    title = it.title,
                    message = it.message,
                    onConfirm = it.onConfirm,
                    onDismiss = onMessageHandled,
                )
            }
            is SettingsMessageState.UserTypeSelection -> {
                ListSelectionBottomSheet(
                    title = stringResource(Res.string.title_select_app_usage_role),
                    helperText = stringResource(Res.string.helper_app_usage_role_sheet),
                    primaryButtonLabel = stringResource(Res.string.btn_save_and_continue),
                    options = userTypeListSelectionOptions(),
                    initialSelection = it.selectedUserType,
                    scrollable = false,
                    onDismiss = onMessageHandled,
                    onConfirm = { userType -> it.onSaveAndContinue(userType) },
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
                is SettingsNavigationState.ResetToRoot -> navigator?.popToRoot()
            }
            onNavigationHandled()
        }
    }
}
