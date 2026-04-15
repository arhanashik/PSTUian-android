package com.workfort.pstuian.ui.forgotpassword

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.workfort.pstuian.common.composable.AppBar
import com.workfort.pstuian.common.composable.ShowErrorDialog
import com.workfort.pstuian.common.composable.ShowLoaderDialog
import com.workfort.pstuian.common.composable.ShowSuccessDialog
import com.workfort.pstuian.common.navigation.AppNavigator
import com.workfort.pstuian.common.navigation.AppScreen
import com.workfort.pstuian.ui.forgotpassword.composable.ForgotPasswordContentPanel
import com.workfort.pstuian.ui.forgotpassword.state.ForgotPasswordMessageState
import com.workfort.pstuian.ui.forgotpassword.state.ForgotPasswordNavigationState
import com.workfort.pstuian.ui.forgotpassword.state.ForgotPasswordUiEvent
import com.workfort.pstuian.ui.forgotpassword.state.ForgotPasswordUiState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_forgot_password_screen

@Composable
fun ForgotPasswordScreen(viewModel: ForgotPasswordViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    ForgotPasswordScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForgotPasswordScreenContent(
    uiState: ForgotPasswordUiState,
    onUiEvent: (ForgotPasswordUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = stringResource(Res.string.label_forgot_password_screen),
                navigation = { onUiEvent(ForgotPasswordUiEvent.BackClicked) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        ForgotPasswordContentPanel(uiState, onUiEvent)

        if (uiState.isLoading) {
            ShowLoaderDialog()
        }
    }
}

@Composable
private fun HandleMessageState(
    message: ForgotPasswordMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is ForgotPasswordMessageState.PasswordResetLinkSentSuccess -> {
                ShowSuccessDialog(
                    message = it.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
            is ForgotPasswordMessageState.Error -> {
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
    navigation: ForgotPasswordNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is ForgotPasswordNavigationState.GoBack -> navigator?.goBack()
                is ForgotPasswordNavigationState.SignIn -> {
                    navigator?.navigateTo(AppScreen.SignIn)
                }
            }
            onNavigationHandled()
        }
    }
}
