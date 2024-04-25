package com.workfort.pstuian.app.ui.mycheckinlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.component.AnimatedEmptyView
import com.workfort.pstuian.app.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.DotView
import com.workfort.pstuian.app.ui.common.component.LoadAsyncImage
import com.workfort.pstuian.app.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.app.ui.common.component.ShowInfoDialog
import com.workfort.pstuian.app.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.app.ui.common.component.TitleTextSmall
import com.workfort.pstuian.app.ui.common.component.isLastItemVisible
import com.workfort.pstuian.app.ui.common.theme.LottieAnimation
import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.model.CheckInPrivacy
import com.workfort.pstuian.util.DateUtil
import com.workfort.pstuian.util.helper.MathUtil


@Composable
fun MyCheckInListScreen(
    modifier: Modifier = Modifier,
    viewModel: MyCheckInListViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<MyCheckInListScreenUiEvent>(MyCheckInListScreenUiEvent.None)
    }

    LaunchedEffect(key1 = null) {
        uiEvent = MyCheckInListScreenUiEvent.OnLoadMoreData(refresh = true)
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
            is MyCheckInListScreenUiEvent.None -> Unit
            is MyCheckInListScreenUiEvent.OnLoadMoreData -> viewModel.loadCheckInList(refresh)
            is MyCheckInListScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is MyCheckInListScreenUiEvent.OnClickItem -> viewModel.onClickItem(item)
            is MyCheckInListScreenUiEvent.OnClickChangePrivacy ->
                viewModel.onClickChangePrivacy(item, privacy)
            is MyCheckInListScreenUiEvent.OnClickDelete -> viewModel.onClickDelete(item)
            is MyCheckInListScreenUiEvent.OnChangePrivacy -> viewModel.changePrivacy(item, privacy)
            is MyCheckInListScreenUiEvent.OnDelete -> viewModel.delete(item)
            is MyCheckInListScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is MyCheckInListScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = MyCheckInListScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: MyCheckInListScreenState.DisplayState,
    onUiEvent: (MyCheckInListScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = context.getString(R.string.txt_my_check_in_list),
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
    val context = LocalContext.current
    val date = DateUtil.getTimeAgo(item.date)
    val checkInCountStr = "${MathUtil.prettyCount(item.count)} check in"
    val privacyTxt = when(item.privacy) {
        CheckInPrivacy.ONLY_ME.value -> context.getString(R.string.txt_only_me)
        else -> context.getString(R.string.txt_public)
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
            placeholder = R.drawable.ic_location_placeholder,
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
        is MyCheckInListScreenState.DisplayState.CheckInListState.Error -> {
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
private fun MyCheckInListScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (MyCheckInListScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
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
                title = context.getString(R.string.txt_update),
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
                title = context.getString(R.string.txt_delete),
                message = context.getString(R.string.msg_delete_permanent),
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
                dismissButtonText = context.getString(R.string.txt_retry),
                onDismiss = {
                    onUiEvent(MyCheckInListScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun MyCheckInListScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (MyCheckInListScreenUiEvent) -> Unit,
) {
    when (this) {
        is MyCheckInListScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
    }
    onUiEvent(MyCheckInListScreenUiEvent.NavigationConsumed)
}