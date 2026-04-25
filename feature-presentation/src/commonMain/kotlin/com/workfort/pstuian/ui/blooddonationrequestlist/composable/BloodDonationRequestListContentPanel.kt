package com.workfort.pstuian.ui.blooddonationrequestlist.composable

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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.featuredomain.model.BloodDonationRequestEntity
import com.workfort.pstuian.ui.blooddonationrequestlist.state.BloodDonationRequestListUiEvent
import com.workfort.pstuian.ui.blooddonationrequestlist.state.BloodDonationRequestListUiState
import com.workfort.pstuian.ui.common.composable.AnimatedEmptyView
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.LoadAsyncUserImage
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.common.composable.isLastItemVisible

@Composable
internal fun BloodDonationRequestListContentPanel(
    uiState: BloodDonationRequestListUiState.Content,
    onUiEvent: (BloodDonationRequestListUiEvent) -> Unit,
) {
    if (uiState.requestList.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.loadError != null) {
                AnimatedErrorView()
            } else {
                AnimatedEmptyView()
            }
        }
    } else {
        RequestListView(
            requestList = uiState.requestList,
            isLoading = uiState.isLoading,
            onUiEvent = onUiEvent,
        )
    }
}

@Composable
private fun RequestListView(
    requestList: List<BloodDonationRequestEntity>,
    isLoading: Boolean,
    onUiEvent: (BloodDonationRequestListUiEvent) -> Unit,
) {
    val listState = rememberLazyListState()
    val isLastItemVisible by remember {
        derivedStateOf {
            listState.isLastItemVisible
        }
    }

    LaunchedEffect(key1 = isLastItemVisible) {
        if (isLastItemVisible) {
            onUiEvent(BloodDonationRequestListUiEvent.LoadMore(refresh = false))
        }
    }

    LazyColumn(
        modifier = Modifier,
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(requestList) { item ->
            RequestListItemView(
                item = item,
                onClickItem = {
                    onUiEvent(BloodDonationRequestListUiEvent.ItemClicked(it))
                },
                onClickCall = {
                    onUiEvent(BloodDonationRequestListUiEvent.CallClicked(it))
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
