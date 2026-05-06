package com.workfort.pstuian.ui.donate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.LoadingOverlay
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.composable.dialog.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowSuccessDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.donate.composable.DonateContentPanel
import com.workfort.pstuian.ui.donate.state.DonateMessageState
import com.workfort.pstuian.ui.donate.state.DonateNavigationState
import com.workfort.pstuian.ui.donate.state.DonateUiEvent
import com.workfort.pstuian.ui.donate.state.DonateUiState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_donate_screen

@Composable
fun DonateScreen(viewModel: DonateViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    DonateScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DonateScreenContent(
    uiState: DonateUiState,
    onUiEvent: (DonateUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    AppScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = stringResource(Res.string.label_donate_screen),
                navigation = {
                    NavigationButton {
                        onUiEvent(DonateUiEvent.BackClicked)
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        Column(modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        ) {
            DonateContentPanel(uiState, onUiEvent)
        }
        if (uiState.isLoading) {
            LoadingOverlay()
        }
    }
}

@Composable
private fun HandleMessageState(
    message: DonateMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is DonateMessageState.ShowAlert -> {
                ShowSuccessDialog(
                    title = it.title,
                    message = it.message,
                    onConfirm = { onMessageHandled() },
                    onDismiss = { onMessageHandled() },
                )
            }
            is DonateMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = { onMessageHandled() },
                    onDismiss = { onMessageHandled() },
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: DonateNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                DonateNavigationState.GoBack -> navigator?.goBack()
            }
            onNavigationHandled()
        }
    }
}