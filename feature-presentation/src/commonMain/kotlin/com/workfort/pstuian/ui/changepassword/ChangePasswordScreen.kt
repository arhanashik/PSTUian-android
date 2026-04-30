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
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.ShowLoaderDialog
import com.workfort.pstuian.ui.common.composable.ShowSuccessDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
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

    HandleMessageState(
        message = message,
        onMessageHandled = viewModel::onMessageHandled,
        onSuccessDismissToSignIn = viewModel::onPostSuccessNavigateToSignIn,
    )
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: ChangePasswordMessageState?,
    onMessageHandled: () -> Unit,
    onSuccessDismissToSignIn: () -> Unit,
) {
    message?.let {
        when (it) {
            is ChangePasswordMessageState.Loader -> {
                ShowLoaderDialog(cancelable = it.cancelable)
            }
            is ChangePasswordMessageState.Success -> {
                ShowSuccessDialog(
                    message = it.message,
                    onDismiss = {
                        onMessageHandled()
                        if (it.navigateToSignInAfterDismiss) onSuccessDismissToSignIn()
                    },
                    onConfirm = {
                        onMessageHandled()
                        if (it.navigateToSignInAfterDismiss) onSuccessDismissToSignIn()
                    },
                )
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
                is ChangePasswordNavigationState.SignIn -> navigator?.resetAll(AppScreen.SignIn)
            }
            onNavigationHandled()
        }
    }
}
