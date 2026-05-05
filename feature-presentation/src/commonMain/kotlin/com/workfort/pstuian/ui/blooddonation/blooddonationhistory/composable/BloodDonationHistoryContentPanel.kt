package com.workfort.pstuian.ui.blooddonation.blooddonationhistory.composable

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.featuredomain.model.BloodDonationEntity
import com.workfort.pstuian.ui.common.composable.AnimatedEmptyView
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.LabelText
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state.BloodDonationHistoryUiEvent
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state.BloodDonationHistoryUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_delete
import pstuian.feature_presentation.generated.resources.txt_edit

@Composable
fun BloodDonationHistoryContentPanel(
    uiState: BloodDonationHistoryUiState.Content,
    onUiEvent: (BloodDonationHistoryUiEvent) -> Unit,
) {
    if (uiState.error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AnimatedErrorView()
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (uiState.donations.isEmpty()) {
            if (uiState.isContentLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AnimatedEmptyView()
                }
            }
        } else {
            DonationListView(
                donations = uiState.donations,
                isLoading = uiState.isContentLoading,
                onUiEvent = onUiEvent,
            )
        }
    }
}

@Composable
private fun DonationListView(
    donations: List<BloodDonationEntity>,
    isLoading: Boolean,
    onUiEvent: (BloodDonationHistoryUiEvent) -> Unit,
) {
    val listState = rememberLazyListState()
    var lastLoadMoreRequestedAtSize by remember { mutableIntStateOf(-1) }
    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            if (totalItems == 0) return@derivedStateOf false
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
            lastVisibleIndex >= totalItems - 1
        }
    }

    LaunchedEffect(shouldLoadMore, isLoading, donations.size) {
        val canRequestMore = shouldLoadMore && !isLoading && donations.isNotEmpty()
        if (canRequestMore && lastLoadMoreRequestedAtSize != donations.size) {
            lastLoadMoreRequestedAtSize = donations.size
            onUiEvent(BloodDonationHistoryUiEvent.LoadMore)
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
                    onUiEvent(BloodDonationHistoryUiEvent.EditClicked(item))
                },
                onClickDelete = {
                    onUiEvent(BloodDonationHistoryUiEvent.DeleteClicked(item))
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
