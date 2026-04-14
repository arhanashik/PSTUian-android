package com.workfort.pstuian.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.common.composable.ShowErrorDialog
import com.workfort.pstuian.common.navigation.AppNavigator
import com.workfort.pstuian.common.navigation.AppScreen
import com.workfort.pstuian.ui.splash.composable.SplashContentPanel
import com.workfort.pstuian.ui.splash.state.SplashMessageState
import com.workfort.pstuian.ui.splash.state.SplashNavigationState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.message_force_refresh_dialog
import pstuian.feature_presentation.generated.resources.message_force_update_dialog
import pstuian.feature_presentation.generated.resources.title_force_refresh_dialog
import pstuian.feature_presentation.generated.resources.title_force_update_dialog

@Composable
internal fun SplashScreen(viewModel: SplashViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    SplashContentPanel(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    messageState: SplashMessageState?,
    onMessageHandled: () -> Unit,
) {
    if (messageState == null) return

    when (messageState) {
        is SplashMessageState.DeviceRegFailed -> {
            ShowErrorDialog(
                message = "Failed to register Device, please try again.",
                dismissButtonText = null,
                cancelable = false,
                onConfirm = messageState.onRetry,
                onDismiss = onMessageHandled,
            )
        }
        is SplashMessageState.GetConfigFailed -> {
            ShowErrorDialog(
                message = "Failed to get configuration, please try again.",
                dismissButtonText = null,
                cancelable = false,
                onConfirm = messageState.onRetry,
                onDismiss = onMessageHandled,
            )
        }
        is SplashMessageState.ForceUpdate -> {
            ShowErrorDialog(
                title = stringResource(Res.string.title_force_update_dialog),
                message = stringResource(Res.string.message_force_update_dialog),
                dismissButtonText = null,
                cancelable = false,
                onConfirm = messageState.onConfirm,
                onDismiss = onMessageHandled,
            )
        }
        is SplashMessageState.ForceRefresh -> {
            ShowErrorDialog(
                title = stringResource(Res.string.title_force_refresh_dialog),
                message = stringResource(Res.string.message_force_refresh_dialog),
                dismissButtonText = null,
                cancelable = false,
                onConfirm = messageState.onConfirm,
                onDismiss = onMessageHandled,
            )
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigationState: SplashNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigationState) {
        navigationState?.let {
            when (it) {
                is SplashNavigationState.HomeScreen -> {
                    navigator?.navigateTo(AppScreen.Home)
                }
            }
            onNavigationHandled()
        }
    }
}
