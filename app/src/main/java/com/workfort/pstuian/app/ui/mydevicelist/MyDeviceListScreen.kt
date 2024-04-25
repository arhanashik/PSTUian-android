package com.workfort.pstuian.app.ui.mydevicelist

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.component.AnimatedEmptyView
import com.workfort.pstuian.app.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.app.ui.common.component.ShowInfoDialog
import com.workfort.pstuian.app.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.app.ui.common.component.TitleTextSmall
import com.workfort.pstuian.app.ui.common.component.isLastItemVisible
import com.workfort.pstuian.app.ui.common.theme.LottieAnimation
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.model.DeviceEntity
import com.workfort.pstuian.util.DateUtil
import kotlinx.coroutines.delay


@Composable
fun MyDeviceListScreen(
    modifier: Modifier = Modifier,
    viewModel: MyDeviceListViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<MyDeviceListScreenUiEvent>(MyDeviceListScreenUiEvent.None)
    }

    LaunchedEffect(key1 = null) {
        uiEvent = MyDeviceListScreenUiEvent.OnLoadMoreData(refresh = true)
    }

    with(screenState) {
        displayState.Handle(modifier = modifier) {
            uiEvent = it
        }
        navigationState?.Handle(navController) {
            uiEvent = it
        }
    }

    with(uiEvent) {
        when (this) {
            is MyDeviceListScreenUiEvent.None -> Unit
            is MyDeviceListScreenUiEvent.OnLoadMoreData -> viewModel.loadDeviceList(refresh)
            is MyDeviceListScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is MyDeviceListScreenUiEvent.OnClickItem -> viewModel.onClickItem(item)
            is MyDeviceListScreenUiEvent.OnClickSignOutFromAll ->
                viewModel.onClickSignOutFromAllDevice()
            is MyDeviceListScreenUiEvent.OnClickDelete -> viewModel.onClickDelete(item)
            is MyDeviceListScreenUiEvent.OnSignOutFromAll -> viewModel.signOutFromAllDevices()
            is MyDeviceListScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is MyDeviceListScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = MyDeviceListScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: MyDeviceListScreenState.DisplayState,
    onUiEvent: (MyDeviceListScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
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
                title = context.getString(R.string.txt_devices),
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
                        Text(text = LocalContext.current.getString(R.string.txt_sign_out_from_all))
                    },
                    onClick = {
                        onUiEvent(MyDeviceListScreenUiEvent.OnClickSignOutFromAll)
                    },
                    icon = {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_sign_out),
                            contentDescription = null,
                        )
                    },
                    shape = CircleShape,
                )
            }
        },
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            displayState.listState.Handle(modifier, onUiEvent)
        }
    }
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
private fun MyDeviceListScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (MyDeviceListScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
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
                        AnimatedEmptyView(modifier = Modifier.width(LottieAnimation.errorWidth))
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
                AnimatedErrorView(modifier = Modifier.width(LottieAnimation.errorWidth))
            }
        }
    }
}

@Composable
private fun MyDeviceListScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (MyDeviceListScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    when (this) {
        is MyDeviceListScreenState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = cancelable)
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
                title = context.getString(R.string.txt_sign_out_from_all),
                message = context.getString(R.string.msg_sign_out_from_all),
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
                dismissButtonText = context.getString(R.string.txt_retry),
                onDismiss = {
                    onUiEvent(MyDeviceListScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun MyDeviceListScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (MyDeviceListScreenUiEvent) -> Unit,
) {
    when (this) {
        is MyDeviceListScreenState.NavigationState.GoBack -> {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(Const.Key.IS_SIGNED_OUT_FROM_ALL_DEVICE, isSignedOutFromAll)
            navController.popBackStack()
        }
    }
    onUiEvent(MyDeviceListScreenUiEvent.NavigationConsumed)
}