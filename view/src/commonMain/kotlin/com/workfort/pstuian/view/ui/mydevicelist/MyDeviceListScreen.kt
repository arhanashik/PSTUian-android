package com.workfort.pstuian.view.ui.mydevicelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.model.DeviceEntity
import com.workfort.pstuian.reducer.ui.mydevicelist.MyDeviceListScreenState
import com.workfort.pstuian.reducer.ui.mydevicelist.MyDeviceListScreenUiEvent
import com.workfort.pstuian.util.DateUtil
import com.workfort.pstuian.view.ui.common.component.AnimatedEmptyView
import com.workfort.pstuian.view.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.view.ui.common.component.AppBar
import com.workfort.pstuian.view.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.view.ui.common.component.ShowInfoDialog
import com.workfort.pstuian.view.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.view.ui.common.component.TitleTextSmall
import com.workfort.pstuian.view.ui.common.component.isLastItemVisible
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import pstuian.shared.generated.resources.Res
import pstuian.shared.generated.resources.msg_sign_out_from_all
import pstuian.shared.generated.resources.txt_devices
import pstuian.shared.generated.resources.txt_retry
import pstuian.shared.generated.resources.txt_sign_out_from_all


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDeviceListScreen(
    modifier: Modifier = Modifier,
    screenState: MyDeviceListScreenState,
    onUiEvent: (MyDeviceListScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var fabButtonExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = null) {
        delay(1000)
        fabButtonExpanded = false
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = stringResource(Res.string.txt_devices),
                actionIcon = Icons.Filled.Refresh,
                onClickBack = {
                    onUiEvent(MyDeviceListScreenUiEvent.OnClickBack)
                },
                onClickAction = {
                    onUiEvent(MyDeviceListScreenUiEvent.OnLoadMoreData(refresh = true))
                },
            )
        },
        floatingActionButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                ExtendedFloatingActionButton(
                    expanded = fabButtonExpanded,
                    text = {
                        Text(text = stringResource(Res.string.txt_sign_out_from_all))
                    },
                    onClick = {
                        onUiEvent(MyDeviceListScreenUiEvent.OnClickSignOutFromAll)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                        )
                    },
                    shape = CircleShape,
                )
            }
        },
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            screenState.displayState.listState.Handle(modifier, onUiEvent)
        }
    }

    screenState.displayState.messageState?.Handle(onUiEvent)
}

@Composable
private fun List<DeviceEntity>.ListView(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onUiEvent: (MyDeviceListScreenUiEvent) -> Unit,
) {
    val listState = rememberLazyListState()
    val isLastItemVisible by remember {
        derivedStateOf {
            listState.isLastItemVisible
        }
    }

    LaunchedEffect(key1 = isLastItemVisible) {
        onUiEvent(MyDeviceListScreenUiEvent.OnLoadMoreData(refresh = false))
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(this@ListView) { item ->
            ListItemView(
                item = item,
                onClick = {
                    onUiEvent(MyDeviceListScreenUiEvent.OnClickItem(item))
                },
            )
        }
        if (isLoading) {
            item {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun ListItemView(
    item: DeviceEntity,
    onClick: () -> Unit,
) {
    val lastActiveAt = DateUtil.getTimeAgo(item.updatedAt?: "")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = "Edit Icon",
            tint = Color.Gray,
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            TitleTextSmall(text = item.model ?: "Unknown")
            Text(text = "IP Address: ${item.ipAddress}")
            Text(text = "Last Activity: $lastActiveAt")
        }
    }
}

@Composable
private fun MyDeviceListScreenState.DisplayState.DeviceListState.Handle(
    modifier: Modifier,
    onUiEvent: (MyDeviceListScreenUiEvent) -> Unit,
) {
    when (this) {
        is MyDeviceListScreenState.DisplayState.DeviceListState.None -> Unit
        is MyDeviceListScreenState.DisplayState.DeviceListState.Available -> {
            if (items.isEmpty()) {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        AnimatedEmptyView(modifier = Modifier.width(200.dp))
                    }
                }
            } else {
                items.ListView(
                    modifier = modifier,
                    isLoading = isLoading,
                    onUiEvent = onUiEvent,
                )
            }
        }
        is MyDeviceListScreenState.DisplayState.DeviceListState.Error -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedErrorView(modifier = Modifier.width(200.dp))
            }
        }
    }
}

@Composable
private fun MyDeviceListScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (MyDeviceListScreenUiEvent) -> Unit,
) {
    when (this) {
        is MyDeviceListScreenState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog()
        }
        is MyDeviceListScreenState.DisplayState.MessageState.ShowDetails -> {
            MyDeviceItemBottomSheet(
                item = item,
                onClickDelete = {
                    onUiEvent(MyDeviceListScreenUiEvent.OnClickDelete(item))
                },
                onDismiss = {
                    onUiEvent(MyDeviceListScreenUiEvent.MessageConsumed)
                },
            )
        }
        is MyDeviceListScreenState.DisplayState.MessageState.ConfirmSignOutFromAll -> {
            ShowConfirmationDialog(
                title = stringResource(Res.string.txt_sign_out_from_all),
                message = stringResource(Res.string.msg_sign_out_from_all),
                onConfirm = {
                    onUiEvent(MyDeviceListScreenUiEvent.OnSignOutFromAll)
                },
                onDismiss = {
                    onUiEvent(MyDeviceListScreenUiEvent.MessageConsumed)
                },
            )
        }
        is MyDeviceListScreenState.DisplayState.MessageState.Success -> {
            ShowInfoDialog(
                message = message,
                onDismiss = {
                    onUiEvent(MyDeviceListScreenUiEvent.MessageConsumed)
                }
            )
        }
        is MyDeviceListScreenState.DisplayState.MessageState.Error -> {
            ShowInfoDialog(
                message = message,
                dismissButtonText = stringResource(Res.string.txt_retry),
                onDismiss = {
                    onUiEvent(MyDeviceListScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}
