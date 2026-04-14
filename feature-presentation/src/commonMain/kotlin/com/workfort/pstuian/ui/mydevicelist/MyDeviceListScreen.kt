package com.workfort.pstuian.ui.mydevicelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.workfort.pstuian.common.composable.AnimatedEmptyView
import com.workfort.pstuian.common.composable.AnimatedErrorView
import com.workfort.pstuian.common.composable.AppBar
import com.workfort.pstuian.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.common.composable.ShowInfoDialog
import com.workfort.pstuian.common.composable.ShowLoaderDialog
import com.workfort.pstuian.common.composable.TitleTextSmall
import com.workfort.pstuian.common.composable.isLastItemVisible
import com.workfort.pstuian.featuredomain.model.DeviceEntity
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListUiEvent
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListUiState
import com.workfort.pstuian.util.DateTimeUtil
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.msg_sign_out_from_all
import pstuian.feature_presentation.generated.resources.txt_devices
import pstuian.feature_presentation.generated.resources.txt_retry
import pstuian.feature_presentation.generated.resources.txt_sign_out_from_all

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDeviceListScreen(
    modifier: Modifier = Modifier,
    screenState: MyDeviceListUiState,
    onUiEvent: (MyDeviceListUiEvent) -> Unit,
) {
    LaunchedEffect(key1 = null) {
        onUiEvent(MyDeviceListUiEvent.OnLoadList(refresh = true))
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var fabButtonExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = null) {
        delay(1000)
        fabButtonExpanded = false
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = stringResource(Res.string.txt_devices),
                actionIcon = Icons.Filled.Refresh,
                onClickBack = {
                    onUiEvent(MyDeviceListUiEvent.OnClickBack)
                },
                onClickAction = {
                    onUiEvent(MyDeviceListUiEvent.OnLoadList(refresh = true))
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                expanded = fabButtonExpanded,
                text = {
                    Text(text = stringResource(Res.string.txt_sign_out_from_all))
                },
                onClick = {
                    onUiEvent(MyDeviceListUiEvent.OnClickSignOutFromAllDevice)
                },
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                    )
                },
                shape = CircleShape,
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (screenState.devices.isEmpty()) {
                if (screenState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (screenState.error != null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        AnimatedErrorView(modifier = Modifier.width(200.dp))
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        AnimatedEmptyView(modifier = Modifier.width(200.dp))
                    }
                }
            } else {
                DeviceListView(
                    devices = screenState.devices,
                    isLoading = screenState.isLoading,
                    onUiEvent = onUiEvent,
                )
            }
        }

        screenState.messageState?.let {
            HandleMessageState(it, onUiEvent)
        }
    }
}

@Composable
private fun DeviceListView(
    devices: List<DeviceEntity>,
    isLoading: Boolean,
    onUiEvent: (MyDeviceListUiEvent) -> Unit,
) {
    val listState = rememberLazyListState()
    val isLastItemVisible by remember {
        derivedStateOf {
            listState.isLastItemVisible
        }
    }

    LaunchedEffect(key1 = isLastItemVisible) {
        if (isLastItemVisible) {
            onUiEvent(MyDeviceListUiEvent.OnLoadList(refresh = false))
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(devices) { item ->
            DeviceItemView(
                item = item,
                onClick = {
                    onUiEvent(MyDeviceListUiEvent.OnClickItem(item))
                },
            )
        }
        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun DeviceItemView(
    item: DeviceEntity,
    onClick: () -> Unit,
) {
    val lastActiveAt = DateTimeUtil.getTimeAgo(item.updatedAt ?: "")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
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
private fun HandleMessageState(
    messageState: MyDeviceListUiState.MessageState,
    onUiEvent: (MyDeviceListUiEvent) -> Unit,
) {
    when (messageState) {
        is MyDeviceListUiState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = messageState.cancelable)
        }
        is MyDeviceListUiState.MessageState.ShowDetails -> {
            MyDeviceItemBottomSheet(
                item = messageState.item,
                onClickDelete = {
                    // Logic for deleting a single device if needed, 
                    // otherwise just dismiss or handle as requested.
                    onUiEvent(MyDeviceListUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(MyDeviceListUiEvent.MessageConsumed)
                },
            )
        }
        is MyDeviceListUiState.MessageState.ConfirmSignOutFromAll -> {
            ShowConfirmationDialog(
                title = stringResource(Res.string.txt_sign_out_from_all),
                message = stringResource(Res.string.msg_sign_out_from_all),
                onConfirm = {
                    onUiEvent(MyDeviceListUiEvent.OnConfirmSignOutFromAll)
                },
                onDismiss = {
                    onUiEvent(MyDeviceListUiEvent.MessageConsumed)
                },
            )
        }
        is MyDeviceListUiState.MessageState.Success -> {
            ShowInfoDialog(
                message = messageState.message,
                onDismiss = {
                    onUiEvent(MyDeviceListUiEvent.MessageConsumed)
                }
            )
        }
        is MyDeviceListUiState.MessageState.Error -> {
            ShowInfoDialog(
                message = messageState.message,
                dismissButtonText = stringResource(Res.string.txt_retry),
                onDismiss = {
                    onUiEvent(MyDeviceListUiEvent.MessageConsumed)
                }
            )
        }
    }
}
