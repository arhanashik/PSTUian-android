package com.workfort.pstuian.ui.myblooddonationlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.workfort.pstuian.common.composable.AnimatedEmptyView
import com.workfort.pstuian.common.composable.AnimatedErrorView
import com.workfort.pstuian.common.composable.AppBar
import com.workfort.pstuian.common.composable.AppBarIconButton
import com.workfort.pstuian.common.composable.LabelText
import com.workfort.pstuian.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.common.composable.ShowInfoDialog
import com.workfort.pstuian.common.composable.ShowLoaderDialog
import com.workfort.pstuian.common.composable.TitleTextSmall
import com.workfort.pstuian.common.composable.isLastItemVisible
import com.workfort.pstuian.featuredomain.model.BloodDonationEntity
import com.workfort.pstuian.ui.myblooddonationlist.state.MyBloodDonationListUiEvent
import com.workfort.pstuian.ui.myblooddonationlist.state.MyBloodDonationListUiState
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.msg_delete_permanent
import pstuian.feature_presentation.generated.resources.txt_create_new
import pstuian.feature_presentation.generated.resources.txt_delete
import pstuian.feature_presentation.generated.resources.txt_edit
import pstuian.feature_presentation.generated.resources.txt_my_donation_list
import pstuian.feature_presentation.generated.resources.txt_retry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBloodDonationListScreen(
    modifier: Modifier = Modifier,
    screenState: MyBloodDonationListUiState,
    onUiEvent: (MyBloodDonationListUiEvent) -> Unit,
) {
    LaunchedEffect(key1 = null) {
        onUiEvent(MyBloodDonationListUiEvent.OnLoadList(refresh = true))
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
                title = stringResource(Res.string.txt_my_donation_list),
                navigation = {
                    onUiEvent(MyBloodDonationListUiEvent.OnClickBack)
                },
                actions = {
                    AppBarIconButton(
                        icon = Icons.Filled.Refresh,
                        onClick = {
                            onUiEvent(MyBloodDonationListUiEvent.OnLoadList(refresh = true))
                        }
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                expanded = fabButtonExpanded,
                text = { Text(text = stringResource(Res.string.txt_create_new)) },
                onClick = {
                    onUiEvent(MyBloodDonationListUiEvent.OnClickCreateRequest)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "",
                    )
                },
                shape = CircleShape,
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (screenState.donations.isEmpty()) {
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
                DonationListView(
                    donations = screenState.donations,
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
private fun DonationListView(
    donations: List<BloodDonationEntity>,
    isLoading: Boolean,
    onUiEvent: (MyBloodDonationListUiEvent) -> Unit,
) {
    val listState = rememberLazyListState()
    val isLastItemVisible by remember {
        derivedStateOf {
            listState.isLastItemVisible
        }
    }

    LaunchedEffect(key1 = isLastItemVisible) {
        if (isLastItemVisible) {
            onUiEvent(MyBloodDonationListUiEvent.OnLoadList(refresh = false))
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(donations) { item ->
            DonationItemView(
                item = item,
                onClickEdit = {
                    onUiEvent(MyBloodDonationListUiEvent.OnClickEdit(item))
                },
                onClickDelete = {
                    onUiEvent(MyBloodDonationListUiEvent.OnClickDelete(item))
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
private fun DonationItemView(
    item: BloodDonationEntity,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
) {
    val requestId = if (item.requestId == null || item.requestId == 0) {
        "Unregistered"
    } else {
        item.requestId.toString()
    }
    val date = item.date.split(" ")[0]
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
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
                verticalAlignment = Alignment.CenterVertically
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
private fun HandleMessageState(
    messageState: MyBloodDonationListUiState.MessageState,
    onUiEvent: (MyBloodDonationListUiEvent) -> Unit,
) {
    when (messageState) {
        is MyBloodDonationListUiState.MessageState.ConfirmDelete -> {
            ShowConfirmationDialog(
                message = stringResource(Res.string.msg_delete_permanent),
                onConfirm = {
                    onUiEvent(MyBloodDonationListUiEvent.OnConfirmDelete(messageState.item))
                },
                onDismiss = {
                    onUiEvent(MyBloodDonationListUiEvent.MessageConsumed)
                }
            )
        }
        is MyBloodDonationListUiState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = messageState.cancelable)
        }
        is MyBloodDonationListUiState.MessageState.Success -> {
            ShowInfoDialog(
                message = messageState.message,
                onDismiss = {
                    onUiEvent(MyBloodDonationListUiEvent.MessageConsumed)
                }
            )
        }
        is MyBloodDonationListUiState.MessageState.Error -> {
            ShowInfoDialog(
                message = messageState.message,
                dismissButtonText = stringResource(Res.string.txt_retry),
                onDismiss = {
                    onUiEvent(MyBloodDonationListUiEvent.MessageConsumed)
                }
            )
        }
    }
}
