package com.workfort.pstuian.app.ui.common.ui.blooddonationrequestlist

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
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.model.BloodDonationRequestEntity
import com.workfort.pstuian.reducer.ui.blooddonationrequestlist.BloodDonationRequestListScreenState
import com.workfort.pstuian.common.component.AnimatedEmptyView
import com.workfort.pstuian.common.component.AnimatedErrorView
import com.workfort.pstuian.common.component.AppBar
import com.workfort.pstuian.common.component.LoadAsyncUserImage
import com.workfort.pstuian.common.component.ShowConfirmationDialog
import com.workfort.pstuian.common.component.ShowInfoDialog
import com.workfort.pstuian.common.component.TitleTextSmall
import com.workfort.pstuian.common.component.isLastItemVisible
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_blood_donation_request_list_screen
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_msg_call
import pstuian.feature_presentation.generated.resources.txt_request_donation
import pstuian.feature_presentation.generated.resources.txt_title_call


@Composable
fun BloodDonationRequestListScreen(
    modifier: Modifier = Modifier,
    screenState: BloodDonationRequestListScreenState,
    onUiEvent: (BloodDonationRequestListScreenUiEvent) -> Unit,
) {
    LaunchedEffect(key1 = null) {
        onUiEvent(BloodDonationRequestListScreenUiEvent.OnLoadMoreData(refresh = true))
    }

    screenState.displayState.Handle(modifier = modifier, onUiEvent = onUiEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: BloodDonationRequestListScreenState.DisplayState,
    onUiEvent: (BloodDonationRequestListScreenUiEvent) -> Unit,
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
                title = stringResource(Res.string.label_blood_donation_request_list_screen),
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
                    text = {  Text(text = stringResource(Res.string.txt_request_donation)) },
                    onClick = {
                        onUiEvent(BloodDonationRequestListScreenUiEvent.OnClickCreateRequest)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "",
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
                        // Using a hardcoded value for now as LottieAnimation is a bit tricky
                        AnimatedEmptyView(modifier = Modifier.width(200.dp))
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
                AnimatedErrorView(modifier = Modifier.width(200.dp))
            }
        }
    }
}

@Composable
private fun BloodDonationRequestListScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (BloodDonationRequestListScreenUiEvent) -> Unit,
) {
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
                title = stringResource(Res.string.txt_title_call),
                message = stringResource(Res.string.txt_msg_call).plus(" $phoneNumber"),
                confirmButtonText = stringResource(Res.string.txt_call),
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
