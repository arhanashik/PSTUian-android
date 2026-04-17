package com.workfort.pstuian.ui.deleteaccount

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.ShowLoaderDialog
import com.workfort.pstuian.ui.common.composable.ShowSuccessDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.deleteaccount.composable.DeleteAccountContentPanel
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountMessageState
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountNavigationState
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountUiEvent
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountUiState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.msg_delete_account
import pstuian.feature_presentation.generated.resources.txt_delete_account

@Composable
fun DeleteAccountScreen(viewModel: DeleteAccountViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    DeleteAccountScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onUiEvent)
    HandleNavigationState(navigation, viewModel::onNavigationConsumed)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteAccountScreenContent(
    uiState: DeleteAccountUiState,
    onUiEvent: (DeleteAccountUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    AppScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = stringResource(Res.string.txt_delete_account),
                navigation = { onUiEvent(DeleteAccountUiEvent.OnClickBack) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        DeleteAccountContentPanel(uiState, onUiEvent)
    }
}

@Composable
private fun HandleMessageState(
    message: DeleteAccountMessageState?,
    onUiEvent: (DeleteAccountUiEvent) -> Unit,
) {
    message?.let {
        when (it) {
            is DeleteAccountMessageState.Loading -> {
                ShowLoaderDialog(cancelable = it.cancelable)
            }
            is DeleteAccountMessageState.ConfirmAccountDelete -> {
                ShowConfirmationDialog(
                    title = stringResource(Res.string.txt_delete_account),
                    message = stringResource(Res.string.msg_delete_account),
                    onConfirm = {
                        onUiEvent(DeleteAccountUiEvent.OnDeleteAccount)
                    },
                    onDismiss = {
                        onUiEvent(DeleteAccountUiEvent.MessageConsumed)
                    }
                )
            }
            is DeleteAccountMessageState.Success -> {
                ShowSuccessDialog(
                    message = it.message,
                    cancelable = false,
                    confirmButtonText = "Request Recovery",
                    dismissButtonText = "Open Home Screen",
                    onConfirm = {
                        onUiEvent(DeleteAccountUiEvent.OnRequestRecovery)
                    },
                    onDismiss = {
                        onUiEvent(DeleteAccountUiEvent.OnResetToHomeScreen)
                    }
                )
            }
            is DeleteAccountMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = {
                        onUiEvent(DeleteAccountUiEvent.MessageConsumed)
                    },
                    onDismiss = {
                        onUiEvent(DeleteAccountUiEvent.MessageConsumed)
                    }
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

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is DeleteAccountNavigationState.GoBack -> navigator?.goBack()
                is DeleteAccountNavigationState.ResetToContactUsScreen -> {
                    navigator?.resetTo(AppScreen.ContactUs)
                }
                is DeleteAccountNavigationState.ResetToHomeScreen -> {
                    navigator?.resetTo(AppScreen.Home)
                }
            }
            onNavigationHandled()
        }
    }
}
