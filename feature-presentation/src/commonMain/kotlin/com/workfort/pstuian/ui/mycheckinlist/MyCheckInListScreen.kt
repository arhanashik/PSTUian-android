package com.workfort.pstuian.app.ui.common.ui.mycheckinlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.model.CheckInPrivacy
import com.workfort.pstuian.reducer.ui.mycheckinlist.MyCheckInListScreenState
import com.workfort.pstuian.util.DateUtil
import com.workfort.pstuian.util.helper.MathUtil
import com.workfort.pstuian.common.component.AnimatedEmptyView
import com.workfort.pstuian.common.component.AnimatedErrorView
import com.workfort.pstuian.common.component.AppBar
import com.workfort.pstuian.common.component.DotView
import com.workfort.pstuian.common.component.LoadAsyncImage
import com.workfort.pstuian.common.component.MaterialButtonToggleGroup
import com.workfort.pstuian.common.component.ShowConfirmationDialog
import com.workfort.pstuian.common.component.ShowInfoDialog
import com.workfort.pstuian.common.component.ShowLoaderDialog
import com.workfort.pstuian.common.component.TitleTextSmall
import com.workfort.pstuian.common.component.isLastItemVisible
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.msg_delete_permanent
import pstuian.feature_presentation.generated.resources.txt_change_privacy
import pstuian.feature_presentation.generated.resources.txt_delete
import pstuian.feature_presentation.generated.resources.txt_dismiss
import pstuian.feature_presentation.generated.resources.txt_my_check_in_list
import pstuian.feature_presentation.generated.resources.txt_only_me
import pstuian.feature_presentation.generated.resources.txt_public
import pstuian.feature_presentation.generated.resources.txt_retry
import pstuian.feature_presentation.generated.resources.txt_update

@Composable
fun MyCheckInListScreen(
    modifier: Modifier = Modifier,
    screenState: MyCheckInListScreenState,
    onUiEvent: (MyCheckInListScreenUiEvent) -> Unit,
) {
    LaunchedEffect(key1 = null) {
        onUiEvent(MyCheckInListScreenUiEvent.OnLoadMoreData(refresh = true))
    }

    with(screenState) {
        displayState.Handle(modifier = modifier, onUiEvent = onUiEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: MyCheckInListScreenState.DisplayState,
    onUiEvent: (MyCheckInListScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = stringResource(Res.string.txt_my_check_in_list),
                actionIcon = Icons.Filled.Refresh,
                onClickBack = {
                    onUiEvent(MyCheckInListScreenUiEvent.OnClickBack)
                },
                onClickAction = {
                    onUiEvent(MyCheckInListScreenUiEvent.OnLoadMoreData(refresh = true))
                },
            )
        },
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            displayState.checkInListState.Handle(modifier, onUiEvent)
        }
    }
}

@Composable
private fun List<CheckInEntity>.ListView(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onUiEvent: (MyCheckInListScreenUiEvent) -> Unit,
) {
    val listState = rememberLazyListState()
    val isLastItemVisible by remember {
        derivedStateOf {
            listState.isLastItemVisible
        }
    }

    LaunchedEffect(key1 = isLastItemVisible) {
        onUiEvent(MyCheckInListScreenUiEvent.OnLoadMoreData(refresh = false))
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
                    onUiEvent(MyCheckInListScreenUiEvent.OnClickItem(item))
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
    item: CheckInEntity,
    onClick: () -> Unit,
) {
    val date = DateUtil.getTimeAgo(item.date)
    val checkInCountStr = "${MathUtil.prettyCount(item.count)} check in"
    val privacyTxt = when(item.privacy) {
        CheckInPrivacy.ONLY_ME.value -> stringResource(Res.string.txt_only_me)
        else -> stringResource(Res.string.txt_public)
    }
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
        LoadAsyncImage(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(46.dp)
                .clip(RoundedCornerShape(8.dp)),
            url = item.locationImageUrl,
            placeholder = Icons.Default.LocationOn,
            contentScale = ContentScale.Crop,
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            TitleTextSmall(text = item.name, fontSize = 14.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = date, fontSize = 12.sp)
                DotView(color = Color.Gray)
                Text(text = checkInCountStr, fontSize = 12.sp)
                DotView(color = Color.Gray)
                Text(text = privacyTxt, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun MyCheckInListScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (MyCheckInListScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun MyCheckInListScreenState.DisplayState.CheckInListState.Handle(
    modifier: Modifier,
    onUiEvent: (MyCheckInListScreenUiEvent) -> Unit,
) {
    when (this) {
        is MyCheckInListScreenState.DisplayState.CheckInListState.None -> Unit
        is MyCheckInListScreenState.DisplayState.CheckInListState.Available -> {
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
        is MyCheckInListScreenState.DisplayState.CheckInListState.Error -> {
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
private fun MyCheckInListScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (MyCheckInListScreenUiEvent) -> Unit,
) {
    when (this) {
        is MyCheckInListScreenState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = cancelable)
        }
        is MyCheckInListScreenState.DisplayState.MessageState.ShowDetails -> {
            MyCheckInItemBottomSheet(
                item = item,
                onClickChangePrivacy = {
                    onUiEvent(MyCheckInListScreenUiEvent.OnClickChangePrivacy(item, it))
                },
                onClickDelete = {
                    onUiEvent(MyCheckInListScreenUiEvent.OnClickDelete(item))
                },
                onDismiss = {
                    onUiEvent(MyCheckInListScreenUiEvent.MessageConsumed)
                },
            )
        }
        is MyCheckInListScreenState.DisplayState.MessageState.ConfirmPrivacyChange -> {
            ShowConfirmationDialog(
                title = stringResource(Res.string.txt_update),
                message = "Are you surely want to change the privacy?",
                onConfirm = {
                    onUiEvent(MyCheckInListScreenUiEvent.OnChangePrivacy(item, privacy))
                },
                onDismiss = {
                    onUiEvent(MyCheckInListScreenUiEvent.MessageConsumed)
                },
            )
        }
        is MyCheckInListScreenState.DisplayState.MessageState.ConfirmDelete -> {
            ShowConfirmationDialog(
                title = stringResource(Res.string.txt_delete),
                message = stringResource(Res.string.msg_delete_permanent),
                onConfirm = {
                    onUiEvent(MyCheckInListScreenUiEvent.OnDelete(item))
                },
                onDismiss = {
                    onUiEvent(MyCheckInListScreenUiEvent.MessageConsumed)
                },
            )
        }
        is MyCheckInListScreenState.DisplayState.MessageState.Success -> {
            ShowInfoDialog(
                message = message,
                onDismiss = {
                    onUiEvent(MyCheckInListScreenUiEvent.MessageConsumed)
                }
            )
        }
        is MyCheckInListScreenState.DisplayState.MessageState.Error -> {
            ShowInfoDialog(
                message = message,
                dismissButtonText = stringResource(Res.string.txt_retry),
                onDismiss = {
                    onUiEvent(MyCheckInListScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCheckInItemBottomSheet(
    item: CheckInEntity,
    onClickChangePrivacy: (CheckInPrivacy) -> Unit,
    onClickDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    val detailsDialogSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val date = DateUtil.getTimeAgo(item.date)
    val checkInCountStr = "${MathUtil.prettyCount(item.count)} check in"
    val privacyItems = listOf(
        stringResource(Res.string.txt_public),
        stringResource(Res.string.txt_only_me),
    )
    val privacyIndex = when(CheckInPrivacy.create(item.privacy)) {
        null, CheckInPrivacy.PUBLIC -> 0
        CheckInPrivacy.ONLY_ME -> 1
    }
    val (selectedPrivacyIndex, onChangePrivacyIndex) = remember { mutableIntStateOf(privacyIndex) }
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = detailsDialogSheetState,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LoadAsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                url = item.locationImageUrl,
                placeholder = Icons.Default.LocationOn,
                contentScale = ContentScale.Crop,
            )
            TitleTextSmall(modifier = Modifier.padding(top = 8.dp), text = item.name)
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = date)
                DotView(modifier = Modifier.padding(horizontal = 8.dp), color = Color.Gray)
                Text(text = checkInCountStr)
            }
            MaterialButtonToggleGroup(
                items = privacyItems,
                selectedIndex = selectedPrivacyIndex,
            ) {
                onChangePrivacyIndex(it)
            }
            HorizontalDivider(
                modifier = Modifier.padding(top = 16.dp),
                color = Color.LightGray.copy(alpha = 0.5f),
            )
            TextButton(
                onClick = {
                    scope.launch { detailsDialogSheetState.hide() }.invokeOnCompletion {
                        val privacy = when (selectedPrivacyIndex) {
                            0 -> CheckInPrivacy.PUBLIC
                            1 -> CheckInPrivacy.ONLY_ME
                            else -> CheckInPrivacy.PUBLIC
                        }
                        onClickChangePrivacy(privacy)
                    }
                },
                enabled = privacyIndex != selectedPrivacyIndex,
            ) {
                Text(text = stringResource(Res.string.txt_change_privacy))
            }
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            TextButton(
                onClick = {
                    scope.launch { detailsDialogSheetState.hide() }.invokeOnCompletion {
                        onClickDelete()
                    }
                }
            ) {
                Text(text = stringResource(Res.string.txt_delete))
            }
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            TextButton(
                onClick = {
                    scope.launch { detailsDialogSheetState.hide() }.invokeOnCompletion {
                        onDismiss()
                    }
                }
            ) {
                Text(text = stringResource(Res.string.txt_dismiss))
            }
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
        }
    }
}
