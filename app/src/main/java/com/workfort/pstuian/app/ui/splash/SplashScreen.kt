package com.workfort.pstuian.app.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.NavItem
import com.workfort.pstuian.app.ui.common.component.CardWithAnimatedBorder
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.TitleTextMedium
import com.workfort.pstuian.util.helper.PlayStoreUtil
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel,
    navController: NavHostController,
    splashDelayMills: Long = 500L,
) {
    val screenState by viewModel.splashScreenState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    var uiEvent by remember {
        mutableStateOf<SplashUiEvent>(SplashUiEvent.None)
    }
    val playStoreUtil = PlayStoreUtil(LocalContext.current)

    LaunchedEffect(key1 = null) {
        delay(splashDelayMills)
        uiEvent = SplashUiEvent.CheckAuth
    }

    with(screenState) {
        displayState.Handle(modifier = modifier) {
            uiEvent = it
        }
        navigationState?.Handle(navController) {
            uiEvent = it
        }
    }

    with(uiEvent) {
        when (this) {
            SplashUiEvent.None -> Unit
            SplashUiEvent.CheckAuth -> viewModel.checkAuth()
            SplashUiEvent.TryDeviceReg -> viewModel.registerDevice()
            SplashUiEvent.TryGetConfig -> viewModel.getConfig()
            SplashUiEvent.UpdateApp -> playStoreUtil.openStore()
            SplashUiEvent.RefreshData -> viewModel.clearAllData()
            SplashUiEvent.MessageConsumed -> viewModel.messageConsumed()
            SplashUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = SplashUiEvent.None
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> Unit
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
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
                    text = LocalContext.current.getString(R.string.app_name),
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
    onUiEvent: (SplashUiEvent) -> Unit,
) {
    ScreenContent(modifier, loadingText)
    messageState?.Handle(onUiEvent)
}

@Composable
private fun SplashScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (SplashUiEvent) -> Unit,
) {
    val context = LocalContext.current
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
                title = context.getString(R.string.title_force_update_dialog),
                message = context.getString(R.string.message_force_update_dialog),
                dismissButtonText = null,
                cancelable = false,
                onConfirm = { onUiEvent(SplashUiEvent.UpdateApp) },
                onDismiss = { onUiEvent(SplashUiEvent.MessageConsumed) },
            )
        }
        is SplashScreenState.DisplayState.MessageState.ForceRefresh -> {
            ShowErrorDialog(
                title = context.getString(R.string.title_force_refresh_dialog),
                message = context.getString(R.string.message_force_refresh_dialog),
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
    navController: NavHostController,
    onUiEvent: (SplashUiEvent) -> Unit,
) {
    when (this) {
        is SplashScreenState.NavigationState.HomeScreen -> {
            navController.navigate(NavItem.Home.route) {
                popUpTo(NavItem.Splash.route) {
                    inclusive = true
                }
            }
        }
    }
    onUiEvent(SplashUiEvent.NavigationConsumed)
}