package com.workfort.pstuian.ui.mydevicelist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppBarIconButton
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.LoadingOverlay
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowInfoDialog
import com.workfort.pstuian.ui.common.composable.ShowLoaderDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.mydevicelist.composable.MyDeviceListContentPanel
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListMessageState
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListNavigationState
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListUiEvent
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListUiState
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.msg_sign_out_from_all
import pstuian.feature_presentation.generated.resources.txt_devices
import pstuian.feature_presentation.generated.resources.txt_retry
import pstuian.feature_presentation.generated.resources.txt_sign_out_from_all

@Composable
fun MyDeviceListScreen(viewModel: MyDeviceListViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    MyDeviceListScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyDeviceListScreenContent(
    uiState: MyDeviceListUiState,
    onUiEvent: (MyDeviceListUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var fabButtonExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = null) {
        delay(1000)
        fabButtonExpanded = false
    }

    AppScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = stringResource(Res.string.txt_devices),
                navigation = { onUiEvent(MyDeviceListUiEvent.BackClicked) },
                actions = {
                    AppBarIconButton(
                        icon = Icons.Filled.Refresh,
                        onClick = {
                            onUiEvent(MyDeviceListUiEvent.RefreshClicked)
                        }
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                expanded = fabButtonExpanded,
                text = { Text(text = stringResource(Res.string.txt_sign_out_from_all)) },
                onClick = {
                    onUiEvent(MyDeviceListUiEvent.SignOutFromAllDeviceClicked)
                },
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                    )
                },
            )
        },
    ) {
        MyDeviceListContentPanel(uiState, onUiEvent)

        if (uiState.isLoading) {
            LoadingOverlay()
        }
    }
}

@Composable
private fun HandleMessageState(
    message: MyDeviceListMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is MyDeviceListMessageState.Loading -> {
                ShowLoaderDialog(cancelable = it.cancelable)
            }
            is MyDeviceListMessageState.ShowDetails -> {
                MyDeviceItemBottomSheet(
                    item = it.item,
                    onClickDelete = {
                        // Handle delete if applicable, currently same as original
                        onMessageHandled()
                    },
                    onDismiss = { onMessageHandled() },
                )
            }
            is MyDeviceListMessageState.ConfirmSignOutFromAll -> {
                ShowConfirmationDialog(
                    title = stringResource(Res.string.txt_sign_out_from_all),
                    message = stringResource(Res.string.msg_sign_out_from_all),
                    onConfirm = {
                        onMessageHandled()
                        it.onConfirm()
                    },
                    onDismiss = { onMessageHandled() }
                )
            }
            is MyDeviceListMessageState.Success -> {
                ShowInfoDialog(
                    message = it.message,
                    onDismiss = { onMessageHandled() }
                )
            }
            is MyDeviceListMessageState.Error -> {
                ShowInfoDialog(
                    message = it.message,
                    dismissButtonText = stringResource(Res.string.txt_retry),
                    onDismiss = { onMessageHandled() }
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: MyDeviceListNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is MyDeviceListNavigationState.GoBack -> navigator?.goBack()
            }
            onNavigationHandled()
        }
    }
}
