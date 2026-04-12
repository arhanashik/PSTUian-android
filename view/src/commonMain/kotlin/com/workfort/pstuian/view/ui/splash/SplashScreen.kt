package com.workfort.pstuian.view.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.view.ui.common.component.CardWithAnimatedBorder
import com.workfort.pstuian.view.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.view.ui.common.component.TitleTextMedium
import com.workfort.pstuian.viewmodel.splash.SplashViewModel
import com.workfort.pstuian.reducer.ui.splash.SplashScreenState
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import pstuian.shared.generated.resources.Res
import pstuian.shared.generated.resources.app_name
import pstuian.shared.generated.resources.message_force_refresh_dialog
import pstuian.shared.generated.resources.message_force_update_dialog
import pstuian.shared.generated.resources.title_force_refresh_dialog
import pstuian.shared.generated.resources.title_force_update_dialog

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel,
    onNavigateToHome: () -> Unit,
    onUpdateApp: () -> Unit,
    splashDelayMills: Long = 500L,
) {
    val screenState by viewModel.splashScreenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<SplashUiEvent>(SplashUiEvent.None)
    }

    LaunchedEffect(key1 = null) {
        delay(splashDelayMills)
        uiEvent = SplashUiEvent.CheckAuth
    }

    with(screenState) {
        displayState.Handle(
            modifier = modifier,
            onUpdateApp = onUpdateApp,
        ) {
            uiEvent = it
        }
        navigationState?.Handle(
            onNavigateToHome = onNavigateToHome,
        ) {
            uiEvent = it
        }
    }

    with(uiEvent) {
        when (this) {
            SplashUiEvent.None -> Unit
            SplashUiEvent.CheckAuth -> viewModel.checkAuth()
            SplashUiEvent.TryDeviceReg -> viewModel.registerDevice()
            SplashUiEvent.TryGetConfig -> viewModel.getConfig()
            SplashUiEvent.UpdateApp -> onUpdateApp()
            SplashUiEvent.RefreshData -> viewModel.clearAllData()
            SplashUiEvent.MessageConsumed -> viewModel.messageConsumed()
            SplashUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = SplashUiEvent.None
    }
}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    loadingText: String,
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
        Text(text = loadingText)
    }
}

@Composable
private fun SplashScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUpdateApp: () -> Unit,
    onUiEvent: (SplashUiEvent) -> Unit,
) {
    ScreenContent(modifier, loadingText)
    messageState?.Handle(onUiEvent)
}

@Composable
private fun SplashScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (SplashUiEvent) -> Unit,
) {
    when (this) {
        is SplashScreenState.DisplayState.MessageState.DeviceRegFailed -> {
            ShowErrorDialog(
                message = "Failed to register Device, please try again.",
                dismissButtonText = null,
                cancelable = false,
                onConfirm = { onUiEvent(SplashUiEvent.TryDeviceReg) },
                onDismiss = { onUiEvent(SplashUiEvent.MessageConsumed) },
            )
        }
        is SplashScreenState.DisplayState.MessageState.GetConfigFailed -> {
            ShowErrorDialog(
                message = "Failed to get configuration, please try again.",
                dismissButtonText = null,
                cancelable = false,
                onConfirm = { onUiEvent(SplashUiEvent.TryGetConfig) },
                onDismiss = { onUiEvent(SplashUiEvent.MessageConsumed) },
            )
        }
        is SplashScreenState.DisplayState.MessageState.ForceUpdate -> {
            ShowErrorDialog(
                title = stringResource(Res.string.title_force_update_dialog),
                message = stringResource(Res.string.message_force_update_dialog),
                dismissButtonText = null,
                cancelable = false,
                onConfirm = { onUiEvent(SplashUiEvent.UpdateApp) },
                onDismiss = { onUiEvent(SplashUiEvent.MessageConsumed) },
            )
        }
        is SplashScreenState.DisplayState.MessageState.ForceRefresh -> {
            ShowErrorDialog(
                title = stringResource(Res.string.title_force_refresh_dialog),
                message = stringResource(Res.string.message_force_refresh_dialog),
                dismissButtonText = null,
                cancelable = false,
                onConfirm = { onUiEvent(SplashUiEvent.RefreshData) },
                onDismiss = { onUiEvent(SplashUiEvent.MessageConsumed) },
            )
        }
    }
}

@Composable
private fun SplashScreenState.NavigationState.Handle(
    onNavigateToHome: () -> Unit,
    onUiEvent: (SplashUiEvent) -> Unit,
) {
    when (this) {
        is SplashScreenState.NavigationState.HomeScreen -> {
            onNavigateToHome()
        }
    }
    onUiEvent(SplashUiEvent.NavigationConsumed)
}