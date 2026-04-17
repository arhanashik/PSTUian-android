package com.workfort.pstuian.ui.mycheckinlist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
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
import com.workfort.pstuian.ui.common.composable.AppBarIconButton
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.LoadingOverlay
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowInfoDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.mycheckinlist.composable.MyCheckInItemBottomSheet
import com.workfort.pstuian.ui.mycheckinlist.composable.MyCheckInListContentPanel
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInListUiEvent
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInListUiState
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInMessageState
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInNavigationState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.msg_delete_permanent
import pstuian.feature_presentation.generated.resources.txt_delete
import pstuian.feature_presentation.generated.resources.txt_my_check_in_list
import pstuian.feature_presentation.generated.resources.txt_retry
import pstuian.feature_presentation.generated.resources.txt_update

@Composable
fun MyCheckInListScreen(
    viewModel: MyCheckInListViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    MyCheckInListScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyCheckInListScreenContent(
    uiState: MyCheckInListUiState,
    onUiEvent: (MyCheckInListUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    AppScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = stringResource(Res.string.txt_my_check_in_list),
                navigation = {
                    onUiEvent(MyCheckInListUiEvent.BackClicked)
                },
                actions = {
                    AppBarIconButton(
                        icon = Icons.Filled.Refresh,
                        onClick = {
                            onUiEvent(MyCheckInListUiEvent.LoadMoreData(refresh = true))
                        }
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        MyCheckInListContentPanel(uiState, onUiEvent)

        if (uiState.isOperationLoading) {
            LoadingOverlay()
        }
    }
}

@Composable
private fun HandleMessageState(
    message: MyCheckInMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is MyCheckInMessageState.ShowDetails -> {
                MyCheckInItemBottomSheet(
                    item = it.item,
                    onClickChangePrivacy = { privacy ->
                        onMessageHandled()
                        it.onClickChangePrivacy(privacy)
                    },
                    onClickDelete = {
                        onMessageHandled()
                        it.onClickDelete()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is MyCheckInMessageState.ConfirmPrivacyChange -> {
                ShowConfirmationDialog(
                    title = stringResource(Res.string.txt_update),
                    message = "Are you surely want to change the privacy?",
                    onConfirm = {
                        onMessageHandled()
                        it.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is MyCheckInMessageState.ConfirmDelete -> {
                ShowConfirmationDialog(
                    title = stringResource(Res.string.txt_delete),
                    message = stringResource(Res.string.msg_delete_permanent),
                    onConfirm = {
                        onMessageHandled()
                        it.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is MyCheckInMessageState.Success -> {
                ShowInfoDialog(message = it.message, onDismiss = onMessageHandled)
            }
            is MyCheckInMessageState.Error -> {
                ShowInfoDialog(
                    message = it.message,
                    dismissButtonText = stringResource(Res.string.txt_retry),
                    onDismiss = onMessageHandled,
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: MyCheckInNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(key1 = navigation) {
        navigation?.let {
            when (it) {
                is MyCheckInNavigationState.GoBack -> navigator?.goBack()
            }
            onNavigationHandled()
        }
    }
}
