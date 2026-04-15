package com.workfort.pstuian.ui.contactus

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
import com.workfort.pstuian.common.composable.AppBar
import com.workfort.pstuian.common.composable.ShowErrorDialog
import com.workfort.pstuian.common.composable.ShowLoaderDialog
import com.workfort.pstuian.common.composable.ShowSuccessDialog
import com.workfort.pstuian.common.navigation.AppNavigator
import com.workfort.pstuian.ui.contactus.composable.ContactUsContentPanel
import com.workfort.pstuian.ui.contactus.state.ContactUsMessageState
import com.workfort.pstuian.ui.contactus.state.ContactUsNavigationState
import com.workfort.pstuian.ui.contactus.state.ContactUsUiEvent
import com.workfort.pstuian.ui.contactus.state.ContactUsUiState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_contact_us
import pstuian.feature_presentation.generated.resources.txt_home

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ContactUsScreen(viewModel: ContactUsViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(Unit) {
        viewModel.onUiReady()
    }

    LaunchedEffect(navigation) {
        when (navigation) {
            is ContactUsNavigationState.GoBack -> {
                navigator?.goBack()
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
                title = stringResource(Res.string.label_contact_us),
                navigation = {
                    viewModel.onUiEvent(ContactUsUiEvent.OnClickBack)
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        when (val state = uiState) {
            is ContactUsUiState.None -> Unit
            is ContactUsUiState.Content -> {
                ContactUsContentPanel(
                    modifier = Modifier.padding(innerPadding),
                    uiState = state,
                    onUiEvent = viewModel::onUiEvent,
                )

                if (state.isLoading) {
                    ShowLoaderDialog()
                }
            }
        }
    }

    message?.let {
        when (it) {
            is ContactUsMessageState.Success -> {
                ShowSuccessDialog(
                    message = it.message,
                    confirmButtonText = stringResource(Res.string.txt_home),
                    onConfirm = {
                        viewModel.onMessageHandled()
                        viewModel.onUiEvent(ContactUsUiEvent.OnClickBack)
                    },
                    onDismiss = viewModel::onMessageHandled,
                )
            }
            is ContactUsMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = viewModel::onMessageHandled,
                    onDismiss = viewModel::onMessageHandled,
                )
            }
        }
    }
}
