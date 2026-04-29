package com.workfort.pstuian.ui.changepassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.changepassword.composable.ChangePasswordScreenContent
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordMessageState
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordNavigationState
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.ShowSuccessDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import org.koin.compose.koinInject

@Composable
fun ChangePasswordScreen(viewModel: ChangePasswordViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onUiReady()
    }

    ChangePasswordScreenContent(uiState = uiState, onUiEvent = viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: ChangePasswordMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is ChangePasswordMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
            is ChangePasswordMessageState.Success -> {
                ShowSuccessDialog(message = it.message, onConfirm = onMessageHandled)
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
