package com.workfort.pstuian.ui.splash.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.common.composable.CardWithAnimatedBorder
import com.workfort.pstuian.common.composable.ShowErrorDialog
import com.workfort.pstuian.common.composable.TitleTextMedium
import com.workfort.pstuian.ui.splash.state.SplashMessageState
import com.workfort.pstuian.ui.splash.state.SplashUiEvent
import com.workfort.pstuian.ui.splash.state.SplashUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.app_name
import pstuian.feature_presentation.generated.resources.message_force_refresh_dialog
import pstuian.feature_presentation.generated.resources.message_force_update_dialog
import pstuian.feature_presentation.generated.resources.title_force_refresh_dialog
import pstuian.feature_presentation.generated.resources.title_force_update_dialog

@Composable
fun SplashContentPanel(
    modifier: Modifier = Modifier,
    state: SplashUiState,
    onEvent: (SplashUiEvent) -> Unit,
) {
    val borderColors = listOf(
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.primary,
    )
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            CardWithAnimatedBorder(
                borderColors = borderColors,
                borderSize = 2.dp,
            ) {
                TitleTextMedium(
                    text = stringResource(Res.string.app_name),
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Text(text = state.loadingText)
    }

    state.messageState?.let { messageState ->
        HandleMessageState(messageState, onEvent)
    }
}

@Composable
private fun HandleMessageState(
    messageState: SplashMessageState,
    onEvent: (SplashUiEvent) -> Unit,
) {
    when (messageState) {
        is SplashMessageState.DeviceRegFailed -> {
            ShowErrorDialog(
                message = "Failed to register Device, please try again.",
                dismissButtonText = null,
                cancelable = false,
                onConfirm = { onEvent(SplashUiEvent.TryDeviceReg) },
                onDismiss = { onEvent(SplashUiEvent.MessageConsumed) },
            )
        }
        is SplashMessageState.GetConfigFailed -> {
            ShowErrorDialog(
                message = "Failed to get configuration, please try again.",
                dismissButtonText = null,
                cancelable = false,
                onConfirm = { onEvent(SplashUiEvent.TryGetConfig) },
                onDismiss = { onEvent(SplashUiEvent.MessageConsumed) },
            )
        }
        is SplashMessageState.ForceUpdate -> {
            ShowErrorDialog(
                title = stringResource(Res.string.title_force_update_dialog),
                message = stringResource(Res.string.message_force_update_dialog),
                dismissButtonText = null,
                cancelable = false,
                onConfirm = { onEvent(SplashUiEvent.UpdateApp) },
                onDismiss = { onEvent(SplashUiEvent.MessageConsumed) },
            )
        }
        is SplashMessageState.ForceRefresh -> {
            ShowErrorDialog(
                title = stringResource(Res.string.title_force_refresh_dialog),
                message = stringResource(Res.string.message_force_refresh_dialog),
                dismissButtonText = null,
                cancelable = false,
                onConfirm = { onEvent(SplashUiEvent.RefreshData) },
                onDismiss = { onEvent(SplashUiEvent.MessageConsumed) },
            )
        }
    }
}
