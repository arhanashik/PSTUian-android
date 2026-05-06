package com.workfort.pstuian.ui.deleteaccount

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import com.workfort.pstuian.ui.common.composable.dialog.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowLoaderDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.deleteaccount.composable.DeleteAccountScreenContent
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountMessageState
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountNavigationState
import org.koin.compose.koinInject

@Composable
fun DeleteAccountScreen(viewModel: DeleteAccountViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    DeleteAccountScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationConsumed)
}

@Composable
private fun HandleMessageState(
    message: DeleteAccountMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is DeleteAccountMessageState.Loading -> {
                ShowLoaderDialog(cancelable = it.cancelable)
            }
            is DeleteAccountMessageState.ConfirmAction -> {
                ShowConfirmationDialog(
                    message = it.message,
                    onConfirm = {
                        onMessageHandled()
                        it.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is DeleteAccountMessageState.Error -> {
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
    navigation: DeleteAccountNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is DeleteAccountNavigationState.GoBack -> navigator?.goBack()
                is DeleteAccountNavigationState.ResetToHomeScreen -> navigator?.resetTo(AppScreen.Home)
                is DeleteAccountNavigationState.OpenUrl -> uriHandler.openUri(it.url)
            }
            onNavigationHandled()
        }
    }
}
