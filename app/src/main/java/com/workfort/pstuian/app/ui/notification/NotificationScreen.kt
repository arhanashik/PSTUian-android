package com.workfort.pstuian.app.ui.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.ErrorText
import com.workfort.pstuian.app.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.app.ui.common.component.isLastItemVisible
import com.workfort.pstuian.model.NotificationEntity
import com.workfort.pstuian.model.NotificationType
import com.workfort.pstuian.util.DateUtil


@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<NotificationUiEvent>(NotificationUiEvent.None)
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
            is NotificationUiEvent.None -> Unit
            is NotificationUiEvent.OnLoadMoreData -> viewModel.getAll(isRefresh = false)
            is NotificationUiEvent.OnClickBack -> viewModel.onClickBack()
            is NotificationUiEvent.OnClickRefresh -> viewModel.getAll()
            is NotificationUiEvent.OnClickNotification ->
                viewModel.onClickNotification(notification)
            is NotificationUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is NotificationUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = NotificationUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationScreenContent(
    modifier: Modifier,
    displayState: NotificationScreenState.DisplayState,
    onUiEvent: (NotificationUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()
    val isLastItemVisible by remember {
        derivedStateOf {
            listState.isLastItemVisible
        }
    }

    LaunchedEffect(key1 = isLastItemVisible) {
        onUiEvent(NotificationUiEvent.OnLoadMoreData)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = LocalContext.current.getString(R.string.label_notification_screen),
                actionIcon = Icons.Filled.Refresh,
                onClickBack = {
                    onUiEvent(NotificationUiEvent.OnClickBack)
                },
                onClickAction = {
                    onUiEvent(NotificationUiEvent.OnClickRefresh)
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            displayState.notificationState.handle(scope = this, onUiEvent = onUiEvent)
        }
    }
}

@Composable
private fun NotificationView(
    notification: NotificationEntity,
    onClickNotification: (notification: NotificationEntity) -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickNotification(notification) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val type = NotificationType.create(notification.type) ?: NotificationType.DEFAULT
            val iconRes = when(type) {
                NotificationType.DEFAULT -> R.drawable.ic_bell_filled
                NotificationType.BLOOD_DONATION -> R.drawable.ic_blood_drop
                NotificationType.NEWS -> R.drawable.ic_newspaper
                NotificationType.HELP -> R.drawable.ic_hand_heart
            }
            Image(
                painterResource(iconRes),
                contentDescription = "",
                contentScale = ContentScale.Inside,
                modifier = Modifier.weight(0.2f),
            )
            Column(
                modifier = Modifier.weight(0.8f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = notification.title ?: "Notification",
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = notification.message,
                    Modifier.padding(top = 8.dp)
                )
                Text(
                    text = DateUtil.getTimeAgo(notification.date?: ""),
                    fontSize = 11.sp,
                    color = Color.Gray,
                )
            }
        }
    }
}

@Composable
private fun NotificationScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (NotificationUiEvent) -> Unit,
) {
    NotificationScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

private fun NotificationScreenState.DisplayState.NotificationState.handle(
    scope: LazyListScope,
    onUiEvent: (NotificationUiEvent) -> Unit,
) {
    when (this) {
        is NotificationScreenState.DisplayState.NotificationState.None -> Unit
        is NotificationScreenState.DisplayState.NotificationState.Available -> {
            if (notifications.isEmpty()) {
                scope.item {
                    Text(
                        text = if (isLoading) {
                            "Loading..."
                        } else {
                            "No Data available"
                        }
                    )
                }
            } else {
                scope.apply {
                    items(notifications) {
                        NotificationView(it) { notification ->
                            onUiEvent(NotificationUiEvent.OnClickNotification(notification))
                        }
                    }
                    if (isLoading) {
                        item {
                            Text(text = "Loading more...")
                        }
                    }
                }
            }
        }
        is NotificationScreenState.DisplayState.NotificationState.Error -> {
            scope.item {
                ErrorText(text = message)
            }
        }
    }
}

@Composable
private fun NotificationScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (NotificationUiEvent) -> Unit,
) {
    when (this) {
        is NotificationScreenState.DisplayState.MessageState.NotificationDetails -> {
            ShowSuccessDialog(
                icon = Icons.Default.Notifications,
                title = notification.title?: "Notification",
                message = notification.message,
                onConfirm = {
                    onUiEvent(NotificationUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun NotificationScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (NotificationUiEvent) -> Unit,
) {
    when (this) {
        is NotificationScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
    }
    onUiEvent(NotificationUiEvent.NavigationConsumed)
}