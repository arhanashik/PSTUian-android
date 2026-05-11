package com.workfort.pstuian.ui.changepassword

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.workfort.pstuian.ui.changepassword.composable.ChangePasswordScreenContent
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordMessageState
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordNavigationState
import com.workfort.pstuian.ui.common.composable.HandleSnackbar
import com.workfort.pstuian.ui.common.composable.dialog.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowLoaderDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import org.koin.compose.koinInject

@Composable
fun ChangePasswordScreen(viewModel: ChangePasswordViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val navigation by viewModel.navigation.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onUiReady()
    }

    ChangePasswordScreenContent(uiState = uiState, snackbarHostState, onUiEvent = viewModel::onUiEvent)

    HandleMessageState(message = message, snackbarHostState, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: ChangePasswordMessageState?,
    snackbarHostState: SnackbarHostState,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is ChangePasswordMessageState.Loader -> {
                ShowLoaderDialog(cancelable = it.cancelable)
            }
            is ChangePasswordMessageState.Success -> {
                HandleSnackbar(it.message, snackbarHostState, onSnackbarShown = onMessageHandled)
            }
            is ChangePasswordMessageState.Error -> {
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
    navigation: ChangePasswordNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(key1 = navigation) {
        navigation?.let {
            when (it) {
                is ChangePasswordNavigationState.GoBack -> navigator?.goBack()
            }
            onNavigationHandled()
        }
    }
}
