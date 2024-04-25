package com.workfort.pstuian.app.ui.blooddonationrequestlist

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.NavItem
import com.workfort.pstuian.app.ui.common.component.AnimatedEmptyView
import com.workfort.pstuian.app.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.LoadAsyncUserImage
import com.workfort.pstuian.app.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.app.ui.common.component.ShowInfoDialog
import com.workfort.pstuian.app.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.app.ui.common.component.TitleTextSmall
import com.workfort.pstuian.app.ui.common.component.isLastItemVisible
import com.workfort.pstuian.app.ui.common.theme.LottieAnimation
import com.workfort.pstuian.model.BloodDonationRequestEntity
import com.workfort.pstuian.util.helper.LinkUtil
import kotlinx.coroutines.delay


@Composable
fun BloodDonationRequestListScreen(
    modifier: Modifier = Modifier,
    viewModel: BloodDonationRequestListViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<BloodDonationRequestListScreenUiEvent>(BloodDonationRequestListScreenUiEvent.None)
    }
    val linkUtil = LinkUtil(LocalContext.current)

    LaunchedEffect(key1 = null) {
        uiEvent = BloodDonationRequestListScreenUiEvent.OnLoadMoreData(refresh = true)
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
            is BloodDonationRequestListScreenUiEvent.None -> Unit
            is BloodDonationRequestListScreenUiEvent.OnLoadMoreData ->
                viewModel.loadDonationRequests(refresh)
            is BloodDonationRequestListScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is BloodDonationRequestListScreenUiEvent.OnClickItem -> viewModel.onClickItem(item)
            is BloodDonationRequestListScreenUiEvent.OnClickCall ->
                viewModel.onClickCall(phoneNumber)
            is BloodDonationRequestListScreenUiEvent.OnClickCreateRequest ->
                viewModel.onClickCreateRequest()
            is BloodDonationRequestListScreenUiEvent.OnCall -> {
                viewModel.messageConsumed()
                linkUtil.callTo(phoneNumber)
            }
            is BloodDonationRequestListScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is BloodDonationRequestListScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = BloodDonationRequestListScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: BloodDonationRequestListScreenState.DisplayState,
    onUiEvent: (BloodDonationRequestListScreenUiEvent) -> Unit,
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
                title = context.getString(R.string.label_blood_donation_request_list_screen),
                actionIcon = Icons.Filled.Refresh,
                onClickBack = {
                    onUiEvent(BloodDonationRequestListScreenUiEvent.OnClickBack)
                },
                onClickAction = {
                    onUiEvent(BloodDonationRequestListScreenUiEvent.OnLoadMoreData(refresh = true))
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
                    text = {  Text(text = context.getString(R.string.txt_request_donation)) },
                    onClick = {
                        onUiEvent(BloodDonationRequestListScreenUiEvent.OnClickCreateRequest)
                    },
                    icon = {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_blood_drop),
                            "",
                        )
                    },
                    shape = CircleShape,
                )
            }
        },
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            displayState.requestListState.Handle(modifier, onUiEvent)
        }
    }
}

@Composable
private fun List<BloodDonationRequestEntity>.RequestListView(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onUiEvent: (BloodDonationRequestListScreenUiEvent) -> Unit,
) {
    val listState = rememberLazyListState()
    val isLastItemVisible by remember {
        derivedStateOf {
            listState.isLastItemVisible
        }
    }

    LaunchedEffect(key1 = isLastItemVisible) {
        onUiEvent(BloodDonationRequestListScreenUiEvent.OnLoadMoreData(refresh = false))
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(this@RequestListView) { item ->
            RequestListItemView(
                item = item,
                onClickItem = {
                    onUiEvent(BloodDonationRequestListScreenUiEvent.OnClickItem(it))
                },
                onClickCall = {
                    onUiEvent(BloodDonationRequestListScreenUiEvent.OnClickCall(it))
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
private fun RequestListItemView(
    item: BloodDonationRequestEntity,
    onClickItem: (BloodDonationRequestEntity) -> Unit,
    onClickCall: (String) -> Unit,
) {
    val date = item.beforeDate.split(" ")[0]
    val contacts = item.contacts.split(",")
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickItem(item) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row {
                LoadAsyncUserImage(url = item.imageUrl, size = 24.dp)
                Spacer(modifier = Modifier.padding(start = 8.dp))
                Text(text = item.name, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.padding(top = 8.dp))
            TitleTextSmall(text = "Need ${item.bloodGroup} blood before $date")
            item.info?.let { Text(text = it) }
            Text(text = "Request id: ${item.id}")
            if (contacts.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(contacts) { item ->
                        AssistChip(
                            onClick = { onClickCall(item) },
                            label = { Text(item) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Call,
                                    contentDescription = "Call Icon",
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            },
                            shape = CircleShape,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BloodDonationRequestListScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (BloodDonationRequestListScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun BloodDonationRequestListScreenState.DisplayState.BloodDonationRequestListState.Handle(
    modifier: Modifier,
    onUiEvent: (BloodDonationRequestListScreenUiEvent) -> Unit,
) {
    when (this) {
        is BloodDonationRequestListScreenState.DisplayState.BloodDonationRequestListState.None -> Unit
        is BloodDonationRequestListScreenState.DisplayState.BloodDonationRequestListState.Available -> {
            if (requestList.isEmpty()) {
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
                requestList.RequestListView(
                    modifier = modifier,
                    isLoading = isLoading,
                    onUiEvent = onUiEvent,
                )
            }
        }
        is BloodDonationRequestListScreenState.DisplayState.BloodDonationRequestListState.Error -> {
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
private fun BloodDonationRequestListScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (BloodDonationRequestListScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    when (this) {
        is BloodDonationRequestListScreenState.DisplayState.MessageState.ShowDetails -> {
            val date = item.beforeDate.split(" ")[0]
            ShowInfoDialog(
                title = "Need ${item.bloodGroup} blood before $date",
                message = item.info.orEmpty(),
                onDismiss = {
                    onUiEvent(BloodDonationRequestListScreenUiEvent.MessageConsumed)
                }
            )
        }
        is BloodDonationRequestListScreenState.DisplayState.MessageState.Call -> {
            ShowConfirmationDialog(
                icon = Icons.Default.Call,
                title = context.getString(R.string.txt_title_call),
                message = context.getString(R.string.txt_msg_call).plus(" $phoneNumber"),
                confirmButtonText = context.getString(R.string.txt_call),
                onConfirm = {
                    onUiEvent(BloodDonationRequestListScreenUiEvent.OnCall(phoneNumber))
                },
                onDismiss = {
                    onUiEvent(BloodDonationRequestListScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun BloodDonationRequestListScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (BloodDonationRequestListScreenUiEvent) -> Unit,
) {
    when (this) {
        is BloodDonationRequestListScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
        is BloodDonationRequestListScreenState.NavigationState.BloodDonationRequestCreateScreen -> {
            navController.navigate(NavItem.BloodDonationRequestCreate.route)
        }
    }
    onUiEvent(BloodDonationRequestListScreenUiEvent.NavigationConsumed)
}