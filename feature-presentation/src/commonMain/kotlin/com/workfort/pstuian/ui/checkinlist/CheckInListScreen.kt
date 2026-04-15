package com.workfort.pstuian.ui.checkinlist

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
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
import com.workfort.pstuian.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.common.composable.ShowErrorDialog
import com.workfort.pstuian.common.composable.ShowInfoDialog
import com.workfort.pstuian.common.composable.ShowLoaderDialog
import com.workfort.pstuian.common.navigation.AppNavigator
import com.workfort.pstuian.common.navigation.AppScreen
import com.workfort.pstuian.ui.checkinlist.composable.CheckInListContentPanel
import com.workfort.pstuian.ui.checkinlist.state.CheckInListMessageState
import com.workfort.pstuian.ui.checkinlist.state.CheckInListNavigationState
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiEvent
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_check_in_screen
import pstuian.feature_presentation.generated.resources.msg_confirm_check_in
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_check_in
import pstuian.feature_presentation.generated.resources.txt_msg_call
import pstuian.feature_presentation.generated.resources.txt_title_call

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInListScreen(viewModel: CheckInListViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(Unit) {
        viewModel.onUiReady()
    }

    LaunchedEffect(navigation) {
        when (val state = navigation) {
            is CheckInListNavigationState.GoBack -> {
                navigator?.goBack()
                viewModel.onNavigationHandled()
            }
            is CheckInListNavigationState.ProfileScreen -> {
                navigator?.navigateTo(AppScreen.Profile(state.userId, state.userType))
                viewModel.onNavigationHandled()
            }
            is CheckInListNavigationState.LocationPickerScreen -> {
                navigator?.navigateTo(AppScreen.LocationPicker)
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
                title = stringResource(Res.string.label_check_in_screen),
                navigation = {
                    viewModel.onUiEvent(CheckInListUiEvent.OnClickBack)
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        when (val state = uiState) {
            is CheckInListUiState.None -> Unit
            is CheckInListUiState.Loading -> {
                ShowLoaderDialog()
            }
            is CheckInListUiState.Error -> {
                // Show Error View if needed, or just a dialog
            }
            is CheckInListUiState.Content -> {
                CheckInListContentPanel(
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
            is CheckInListMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = viewModel::onMessageHandled,
                    onDismiss = viewModel::onMessageHandled,
                )
            }
            is CheckInListMessageState.Success -> {
                ShowInfoDialog(
                    message = it.message,
                    onDismiss = viewModel::onMessageHandled,
                )
            }
            is CheckInListMessageState.Call -> {
                ShowConfirmationDialog(
                    icon = Icons.Default.Notifications,
                    title = stringResource(Res.string.txt_title_call),
                    message = stringResource(Res.string.txt_msg_call).plus(" ${it.phoneNumber}"),
                    confirmButtonText = stringResource(Res.string.txt_call),
                    onConfirm = {
                        // Handle Call Action
                        viewModel.onMessageHandled()
                    },
                    onDismiss = viewModel::onMessageHandled
                )
            }
            is CheckInListMessageState.ConfirmCheckIn -> {
                ShowConfirmationDialog(
                    icon = Icons.Default.Notifications,
                    message = stringResource(Res.string.msg_confirm_check_in)
                        .plus(" ${it.location.name}"),
                    confirmButtonText = stringResource(Res.string.txt_check_in),
                    onConfirm = {
                        viewModel.checkIn(it.location.id)
                        viewModel.onMessageHandled()
                    },
                    onDismiss = viewModel::onMessageHandled
                )
            }
        }
    }
}
