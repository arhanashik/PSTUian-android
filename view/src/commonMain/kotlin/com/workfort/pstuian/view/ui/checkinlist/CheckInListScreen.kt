package com.workfort.pstuian.view.ui.checkinlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.model.CheckInLocationEntity
import com.workfort.pstuian.reducer.ui.checkinlist.CheckInListScreenState
import com.workfort.pstuian.view.ui.common.component.AnimatedEmptyView
import com.workfort.pstuian.view.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.view.ui.common.component.AppBar
import com.workfort.pstuian.view.ui.common.component.DotView
import com.workfort.pstuian.view.ui.common.component.LoadAsyncImage
import com.workfort.pstuian.view.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.view.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.view.ui.common.component.ShowInfoDialog
import com.workfort.pstuian.view.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.view.ui.common.component.TitleTextSmall
import com.workfort.pstuian.view.ui.common.component.isLastItemVisible
import org.jetbrains.compose.resources.stringResource
import pstuian.shared.generated.resources.Res
import pstuian.shared.generated.resources.img_placeholder_profile
import pstuian.shared.generated.resources.label_check_in_screen
import pstuian.shared.generated.resources.msg_confirm_check_in
import pstuian.shared.generated.resources.txt_call
import pstuian.shared.generated.resources.txt_check_in
import pstuian.shared.generated.resources.txt_msg_call
import pstuian.shared.generated.resources.txt_title_call


@Composable
fun CheckInListScreen(
    modifier: Modifier = Modifier,
    screenState: CheckInListScreenState,
    onUiEvent: (CheckInListScreenUiEvent) -> Unit,
) {
    screenState.displayState.Handle(modifier = modifier, onUiEvent = onUiEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: CheckInListScreenState.DisplayState,
    onUiEvent: (CheckInListScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()
    val isLastItemVisible by remember { derivedStateOf { listState.isLastItemVisible } }

    LaunchedEffect(key1 = isLastItemVisible) {
        onUiEvent(CheckInListScreenUiEvent.OnLoadCheckInListList(isRefresh = false))
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = stringResource(Res.string.label_check_in_screen),
                onClickBack = {
                    onUiEvent(CheckInListScreenUiEvent.OnClickBack)
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
        ) {
            displayState.checkInLocation?.let { location ->
                CheckInListHeaderView(
                    checkInLocation = location,
                    onClickChangeLocation = {
                        onUiEvent(CheckInListScreenUiEvent.OnClickChangeLocation)
                    },
                    onClickCheckIn = {
                        onUiEvent(CheckInListScreenUiEvent.OnClickCheckInList)
                    },
                )
            }
            displayState.checkInListState.Handle(modifier, onUiEvent)
        }
    }
}

@Composable
private fun List<CheckInEntity>.CheckInListView(
    modifier: Modifier,
    isLoading: Boolean,
    onClickItem: (CheckInEntity) -> Unit,
    onClickCall: (String) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(this@CheckInListView) { item ->
            CheckInListItemView(
                item = item,
                onClickItem = { onClickItem(item) },
                onClickCall = { item.phone?.let(onClickCall) }
            )
        }
        if (isLoading) {
            item { CircularProgressIndicator() }
        }
    }
}

@Composable
private fun CheckInListItemView(
    item: CheckInEntity,
    onClickItem: () -> Unit,
    onClickCall: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickItem() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box {
                LoadAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    url = item.imageUrl,
                    placeholder = Res.drawable.img_placeholder_profile,
                    contentScale = ContentScale.Crop,
                )
                DotView(modifier = Modifier.padding(16.dp))
                item.phone?.let {
                    IconButton(
                        modifier = Modifier.align(Alignment.BottomStart),
                        onClick = { onClickCall() },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.White.copy(alpha = 0.2f),
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call Icon",
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                TitleTextSmall(text = item.name, fontSize = 16.sp)
                Text(text = item.batch, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun CheckInListScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (CheckInListScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun CheckInListScreenState.DisplayState.CheckInListState.Handle(
    modifier: Modifier,
    onUiEvent: (CheckInListScreenUiEvent) -> Unit,
) {
    when (this) {
        is CheckInListScreenState.DisplayState.CheckInListState.None -> Unit
        is CheckInListScreenState.DisplayState.CheckInListState.Available -> {
            if (checkInList.isEmpty()) {
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
                checkInList.CheckInListView(
                    modifier = modifier,
                    isLoading = isLoading,
                    onClickItem = {
                        onUiEvent(CheckInListScreenUiEvent.OnClickItem(it))
                    },
                    onClickCall = {
                        onUiEvent(CheckInListScreenUiEvent.OnClickCall(it))
                    },
                )
            }
        }
        is CheckInListScreenState.DisplayState.CheckInListState.Error -> {
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
private fun CheckInListHeaderView(
    checkInLocation: CheckInLocationEntity,
    onClickChangeLocation: () -> Unit,
    onClickCheckIn: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 14.sp)) {
                append("Showing peoples in ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                append(checkInLocation.name)
            }
        }
        Text(text = annotatedString)
        Row {
            AssistChip(
                onClick = { onClickChangeLocation() },
                label = { Text("Change Location") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Change Location Icon",
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                shape = CircleShape,
            )
            Spacer(modifier = Modifier.padding(start = 8.dp))
            AssistChip(
                onClick = { onClickCheckIn() },
                label = { Text("Check In") },
                leadingIcon = {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Check In Icon",
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                shape = CircleShape,
            )
        }
    }
}

@Composable
private fun CheckInListScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (CheckInListScreenUiEvent) -> Unit,
) {
    when (this) {
        is CheckInListScreenState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = cancelable)
        }
        is CheckInListScreenState.DisplayState.MessageState.Call -> {
            ShowConfirmationDialog(
                icon = Icons.Default.Notifications,
                title = stringResource(Res.string.txt_title_call),
                message = stringResource(Res.string.txt_msg_call).plus(" $phoneNumber"),
                confirmButtonText = stringResource(Res.string.txt_call),
                onConfirm = {
                    onUiEvent(CheckInListScreenUiEvent.OnCall(phoneNumber))
                },
                onDismiss = {
                    onUiEvent(CheckInListScreenUiEvent.MessageConsumed)
                }
            )
        }
        is CheckInListScreenState.DisplayState.MessageState.ConfirmCheckIn -> {
            ShowConfirmationDialog(
                icon = Icons.Default.Notifications,
                message = stringResource(Res.string.msg_confirm_check_in)
                    .plus(" ${location.name}"),
                confirmButtonText = stringResource(Res.string.txt_check_in),
                onConfirm = {
                    onUiEvent(CheckInListScreenUiEvent.OnCheckIn(location.id))
                },
                onDismiss = {
                    onUiEvent(CheckInListScreenUiEvent.MessageConsumed)
                }
            )
        }
        is CheckInListScreenState.DisplayState.MessageState.Success -> {
            ShowInfoDialog(
                message = message,
                onDismiss = {
                    onUiEvent(CheckInListScreenUiEvent.MessageConsumed)
                }
            )
        }
        is CheckInListScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(CheckInListScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(CheckInListScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}
