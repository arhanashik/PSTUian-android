package com.workfort.pstuian.ui.mycheckinlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.common.composable.AnimatedErrorView
import com.workfort.pstuian.common.composable.AppBar
import com.workfort.pstuian.common.composable.AppBarIconButton
import com.workfort.pstuian.common.composable.AppScaffold
import com.workfort.pstuian.common.composable.LoadingOverlay
import com.workfort.pstuian.common.composable.NavigationButton
import com.workfort.pstuian.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.common.composable.ShowInfoDialog
import com.workfort.pstuian.common.navigation.AppNavigator
import com.workfort.pstuian.ui.mycheckinlist.composable.MyCheckInItemBottomSheet
import com.workfort.pstuian.ui.mycheckinlist.composable.MyCheckInListContentPanel
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInMessageState
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInListUiEvent
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInListUiState
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
internal fun MyCheckInListScreen(
    viewModel: MyCheckInListViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    MyCheckInListScreenContent(
        uiState = uiState,
        onUiEvent = viewModel::onUiEvent,
    )

    HandleMessageState(
        message = message,
        onUiEvent = viewModel::onUiEvent,
    )

    HandleNavigationState(
        navigation = navigation,
        onNavigationHandled = viewModel::onNavigationHandled,
    )
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
                    NavigationButton {
                        onUiEvent(MyCheckInListUiEvent.BackClicked)
                    }
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
        when (uiState) {
            is MyCheckInListUiState.None -> Unit
            is MyCheckInListUiState.Content -> {
                MyCheckInListContentPanel(
                    uiState = uiState,
                    onUiEvent = onUiEvent,
                )

                if (uiState.isOperationLoading) {
                    LoadingOverlay()
                }
            }
            is MyCheckInListUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedErrorView(modifier = Modifier.width(200.dp))
                }
            }
        }
    }
}

@Composable
private fun HandleMessageState(
    message: MyCheckInMessageState?,
    onUiEvent: (MyCheckInListUiEvent) -> Unit,
) {
    message?.let {
        when (it) {
            is MyCheckInMessageState.ShowDetails -> {
                MyCheckInItemBottomSheet(
                    item = it.item,
                    onClickChangePrivacy = { privacy ->
                        onUiEvent(MyCheckInListUiEvent.ChangePrivacyClicked(it.item, privacy))
                    },
                    onClickDelete = {
                        onUiEvent(MyCheckInListUiEvent.DeleteClicked(it.item))
                    },
                    onDismiss = {
                        onUiEvent(MyCheckInListUiEvent.MessageConsumed)
                    },
                )
            }
            is MyCheckInMessageState.ConfirmPrivacyChange -> {
                ShowConfirmationDialog(
                    title = stringResource(Res.string.txt_update),
                    message = "Are you surely want to change the privacy?",
                    onConfirm = {
                        onUiEvent(MyCheckInListUiEvent.ChangePrivacy(it.item, it.privacy))
                    },
                    onDismiss = {
                        onUiEvent(MyCheckInListUiEvent.MessageConsumed)
                    },
                )
            }
            is MyCheckInMessageState.ConfirmDelete -> {
                ShowConfirmationDialog(
                    title = stringResource(Res.string.txt_delete),
                    message = stringResource(Res.string.msg_delete_permanent),
                    onConfirm = {
                        onUiEvent(MyCheckInListUiEvent.Delete(it.item))
                    },
                    onDismiss = {
                        onUiEvent(MyCheckInListUiEvent.MessageConsumed)
                    },
                )
            }
            is MyCheckInMessageState.Success -> {
                ShowInfoDialog(
                    message = it.message,
                    onDismiss = {
                        onUiEvent(MyCheckInListUiEvent.MessageConsumed)
                    }
                )
            }
            is MyCheckInMessageState.Error -> {
                ShowInfoDialog(
                    message = it.message,
                    dismissButtonText = stringResource(Res.string.txt_retry),
                    onDismiss = {
                        onUiEvent(MyCheckInListUiEvent.MessageConsumed)
                    }
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
