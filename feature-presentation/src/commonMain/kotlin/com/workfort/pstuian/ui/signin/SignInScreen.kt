package com.workfort.pstuian.ui.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.signin.composable.SignInScreenContent
import com.workfort.pstuian.ui.signin.state.SignInMessageState
import com.workfort.pstuian.ui.signin.state.SignInNavigationState
import org.koin.compose.koinInject

@Composable
fun SignInScreen(viewModel: SignInViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    SignInScreenContent(uiState = uiState, onUiEvent = viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: SignInMessageState?,
    onNavigationHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is SignInMessageState.Success -> {
                onNavigationHandled()
            }
            is SignInMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = onNavigationHandled,
                    onDismiss = onNavigationHandled,
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: SignInNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(key1 = navigation) {
        navigation?.let {
            when (it) {
                is SignInNavigationState.GoBack -> navigator?.goBack()
                is SignInNavigationState.GoToEmailVerificationScreen -> {
                    navigator?.navigateTo(AppScreen.EmailVerification)
                }
            }
            onNavigationHandled()
        }
    }
}
