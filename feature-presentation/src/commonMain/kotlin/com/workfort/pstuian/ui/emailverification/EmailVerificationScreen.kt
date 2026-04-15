package com.workfort.pstuian.ui.emailverification

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.workfort.pstuian.common.composable.AppBar
import com.workfort.pstuian.common.composable.AppScaffold
import com.workfort.pstuian.common.composable.ShowErrorDialog
import com.workfort.pstuian.common.composable.ShowLoaderDialog
import com.workfort.pstuian.common.composable.ShowSuccessDialog
import com.workfort.pstuian.common.navigation.AppNavigator
import com.workfort.pstuian.ui.emailverification.composable.EmailVerificationContentPanel
import com.workfort.pstuian.ui.emailverification.state.EmailVerificationMessageState
import com.workfort.pstuian.ui.emailverification.state.EmailVerificationNavigationState
import com.workfort.pstuian.ui.emailverification.state.EmailVerificationUiEvent
import com.workfort.pstuian.ui.emailverification.state.EmailVerificationUiState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_email_verification_screen
import pstuian.feature_presentation.generated.resources.txt_sign_in

@Composable
internal fun EmailVerificationScreen(viewModel: EmailVerificationViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    EmailVerificationScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onUiEvent)
    HandleNavigationState(navigation, viewModel::onNavigationConsumed)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmailVerificationScreenContent(
    uiState: EmailVerificationUiState,
    onUiEvent: (EmailVerificationUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    AppScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = stringResource(Res.string.label_email_verification_screen),
                navigation = { onUiEvent(EmailVerificationUiEvent.OnClickBack) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        EmailVerificationContentPanel(uiState, onUiEvent)
        if (uiState.isLoading) {
            ShowLoaderDialog()
        }
    }
}

@Composable
private fun HandleMessageState(
    message: EmailVerificationMessageState?,
    onUiEvent: (EmailVerificationUiEvent) -> Unit,
) {
    message?.let {
        when (it) {
            is EmailVerificationMessageState.EmailSentSuccess -> {
                ShowSuccessDialog(
                    message = it.message,
                    confirmButtonText = stringResource(Res.string.txt_sign_in),
                    cancelable = false,
                    onConfirm = {
                        onUiEvent(EmailVerificationUiEvent.MessageConsumed)
                        onUiEvent(EmailVerificationUiEvent.OnClickBack)
                    }
                )
            }
            is EmailVerificationMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    dismissButtonText = null,
                    onConfirm = {
                        onUiEvent(EmailVerificationUiEvent.MessageConsumed)
                    },
                    onDismiss = { /* NO OP */ }
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: EmailVerificationNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is EmailVerificationNavigationState.GoBack -> navigator?.goBack()
            }
            onNavigationHandled()
        }
    }
}
