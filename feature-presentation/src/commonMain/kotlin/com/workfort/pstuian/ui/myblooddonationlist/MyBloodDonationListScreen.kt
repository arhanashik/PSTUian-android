package com.workfort.pstuian.app.ui.common.ui.myblooddonationlist

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.model.BloodDonationEntity
import com.workfort.pstuian.reducer.ui.myblooddonationlist.MyBloodDonationListScreenState
import com.workfort.pstuian.common.component.AnimatedEmptyView
import com.workfort.pstuian.common.component.AnimatedErrorView
import com.workfort.pstuian.common.component.AppBar
import com.workfort.pstuian.common.component.LabelText
import com.workfort.pstuian.common.component.ShowConfirmationDialog
import com.workfort.pstuian.common.component.ShowInfoDialog
import com.workfort.pstuian.common.component.ShowLoaderDialog
import com.workfort.pstuian.common.component.TitleTextSmall
import com.workfort.pstuian.common.component.isLastItemVisible
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.msg_delete_permanent
import pstuian.feature_presentation.generated.resources.txt_create_new
import pstuian.feature_presentation.generated.resources.txt_delete
import pstuian.feature_presentation.generated.resources.txt_edit
import pstuian.feature_presentation.generated.resources.txt_my_donation_list
import pstuian.feature_presentation.generated.resources.txt_retry


@Composable
fun MyBloodDonationListScreen(
    modifier: Modifier = Modifier,
    screenState: MyBloodDonationListScreenState,
    onUiEvent: (MyBloodDonationListScreenUiEvent) -> Unit,
) {
    LaunchedEffect(key1 = null) {
        onUiEvent(MyBloodDonationListScreenUiEvent.OnLoadMoreData(refresh = true))
    }

    screenState.displayState.Handle(modifier = modifier, onUiEvent = onUiEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: MyBloodDonationListScreenState.DisplayState,
    onUiEvent: (MyBloodDonationListScreenUiEvent) -> Unit,
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
                title = stringResource(Res.string.txt_my_donation_list),
                actionIcon = Icons.Filled.Refresh,
                onClickBack = {
                    onUiEvent(MyBloodDonationListScreenUiEvent.OnClickBack)
                },
                onClickAction = {
                    onUiEvent(MyBloodDonationListScreenUiEvent.OnLoadMoreData(refresh = true))
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
                    text = {  Text(text = stringResource(Res.string.txt_create_new)) },
                    onClick = {
                        onUiEvent(MyBloodDonationListScreenUiEvent.OnClickCreateRequest)
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
            displayState.listState.Handle(modifier, onUiEvent)
        }
    }
}

@Composable
private fun List<BloodDonationEntity>.ListView(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onUiEvent: (MyBloodDonationListScreenUiEvent) -> Unit,
) {
    val listState = rememberLazyListState()
    val isLastItemVisible by remember {
        derivedStateOf {
            listState.isLastItemVisible
        }
    }

    LaunchedEffect(key1 = isLastItemVisible) {
        onUiEvent(MyBloodDonationListScreenUiEvent.OnLoadMoreData(refresh = false))
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
                onClickEdit = {
                    onUiEvent(MyBloodDonationListScreenUiEvent.OnClickEdit(item))
                },
                onClickDelete = {
                    onUiEvent(MyBloodDonationListScreenUiEvent.OnClickDelete(item))
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
    item: BloodDonationEntity,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
) {
    val requestId = if(item.requestId == null || item.requestId == 0) {
        "Unregistered"
    } else {
        item.requestId.toString()
    }
    val date = item.date.split(" ")[0]
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(),
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
            TitleTextSmall(text = "Request Id: $requestId", fontSize = 14.sp)
            item.info?.let { Text(text = it) }
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                LabelText(text = date)
                Spacer(modifier = Modifier.weight(1f))
                AssistChip(
                    onClick = { onClickEdit() },
                    label = { Text(stringResource(Res.string.txt_edit)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            Modifier.size(AssistChipDefaults.IconSize)
                        )
                    },
                    shape = CircleShape,
                )
                Spacer(modifier = Modifier.padding(start = 8.dp))
                AssistChip(
                    onClick = { onClickDelete() },
                    label = { Text(stringResource(Res.string.txt_delete)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = null,
                            Modifier.size(AssistChipDefaults.IconSize)
                        )
                    },
                    shape = CircleShape,
                )
            }
        }
    }
}

@Composable
private fun MyBloodDonationListScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (MyBloodDonationListScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun MyBloodDonationListScreenState.DisplayState.BloodDonationListState.Handle(
    modifier: Modifier,
    onUiEvent: (MyBloodDonationListScreenUiEvent) -> Unit,
) {
    when (this) {
        is MyBloodDonationListScreenState.DisplayState.BloodDonationListState.None -> Unit
        is MyBloodDonationListScreenState.DisplayState.BloodDonationListState.Available -> {
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
        is MyBloodDonationListScreenState.DisplayState.BloodDonationListState.Error -> {
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
private fun MyBloodDonationListScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (MyBloodDonationListScreenUiEvent) -> Unit,
) {
    when (this) {
        is MyBloodDonationListScreenState.DisplayState.MessageState.ConfirmDelete -> {
            ShowConfirmationDialog(
                message = stringResource(Res.string.msg_delete_permanent),
                onConfirm = {
                    onUiEvent(MyBloodDonationListScreenUiEvent.OnDelete(item))
                },
                onDismiss = {
                    onUiEvent(MyBloodDonationListScreenUiEvent.MessageConsumed)
                }
            )
        }
        is MyBloodDonationListScreenState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = cancelable)
        }
        is MyBloodDonationListScreenState.DisplayState.MessageState.Success -> {
            ShowInfoDialog(
                message = message,
                onDismiss = {
                    onUiEvent(MyBloodDonationListScreenUiEvent.MessageConsumed)
                }
            )
        }
        is MyBloodDonationListScreenState.DisplayState.MessageState.Failure -> {
            ShowInfoDialog(
                message = message,
                dismissButtonText = stringResource(Res.string.txt_retry),
                onDismiss = {
                    onUiEvent(MyBloodDonationListScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}
