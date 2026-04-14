package com.workfort.pstuian.ui.changepassword

import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.common.composable.AppBar
import com.workfort.pstuian.common.composable.ShowErrorDialog
import com.workfort.pstuian.common.composable.ShowLoaderDialog
import com.workfort.pstuian.common.composable.ShowSuccessDialog
import com.workfort.pstuian.common.navigation.AppNavigator
import com.workfort.pstuian.ui.changepassword.composable.ChangePasswordContentPanel
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordMessageState
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordNavigationState
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiEvent
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_change_password

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel,
    navigator: AppNavigator,
) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onUiReady()
    }

    LaunchedEffect(navigation) {
        when (val state = navigation) {
            is ChangePasswordNavigationState.GoBack -> {
                navigator.goBack()
                viewModel.onNavigationHandled()
            }
            null -> Unit
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = stringResource(Res.string.txt_change_password),
                onClickBack = {
                    viewModel.onUiEvent(ChangePasswordUiEvent.BackClicked)
                },
                elevation = 0.dp,
            )
        },
    ) { innerPadding ->
        when (val state = uiState) {
            is ChangePasswordUiState.None -> Unit
            is ChangePasswordUiState.Content -> {
                ChangePasswordContentPanel(
                    modifier = Modifier.padding(innerPadding),
                    uiState = state,
                    onUiEvent = viewModel::onUiEvent,
                )

                if (state.isOperationLoading) {
                    ShowLoaderDialog()
                }
            }
        }
    }

    message?.let {
        when (it) {
            is ChangePasswordMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = viewModel::onMessageHandled,
                    onDismiss = viewModel::onMessageHandled,
                )
            }
            is ChangePasswordMessageState.Success -> {
                ShowSuccessDialog(
                    message = it.message,
                    onConfirm = viewModel::onMessageHandled,
                    onDismiss = viewModel::onMessageHandled,
                )
            }
        }
    }
}
