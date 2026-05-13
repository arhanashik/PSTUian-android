package com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.screendata.BloodDonationRequestDisplayData
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state.BloodDonationRequestListUiEvent
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state.BloodDonationRequestListUiState
import com.workfort.pstuian.ui.common.composable.AnimatedEmptyView
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.LoadAsyncUserImage
import com.workfort.pstuian.ui.common.composable.shimmerAnimation
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import org.jetbrains.compose.resources.stringResource
import pstuian.presentation.generated.resources.Res
import pstuian.presentation.generated.resources.blood_donation_request_mark_as_complete
import pstuian.presentation.generated.resources.blood_donation_request_status_active
import pstuian.presentation.generated.resources.blood_donation_request_status_pending
import pstuian.presentation.generated.resources.txt_call
import pstuian.presentation.generated.resources.txt_donate

@Composable
internal fun BloodDonationRequestListContentPanel(
    uiState: BloodDonationRequestListUiState.Content,
    onUiEvent: (BloodDonationRequestListUiEvent) -> Unit,
) {
    if (uiState.error != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedErrorView()
        }
        return
    }

    if (uiState.requestList.isEmpty()) {
        if (uiState.isLoading) {
            BloodDonationRequestListShimmer()
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
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
    requestList: List<BloodDonationRequestDisplayData>,
    isLoading: Boolean,
    onUiEvent: (BloodDonationRequestListUiEvent) -> Unit,
) {
    val listState = rememberLazyListState()
    var lastLoadMoreRequestedAtSize by remember { mutableIntStateOf(-1) }
    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            if (totalItems == 0) return@derivedStateOf false
            val lastVisibleIndex =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
            lastVisibleIndex >= totalItems - 1
        }
    }

    LaunchedEffect(shouldLoadMore, isLoading, requestList.size) {
        val canRequestMore = shouldLoadMore && !isLoading && requestList.isNotEmpty()
        if (canRequestMore && lastLoadMoreRequestedAtSize != requestList.size) {
            lastLoadMoreRequestedAtSize = requestList.size
            onUiEvent(BloodDonationRequestListUiEvent.LoadMore)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
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
                onClickMarkAsComplete = {
                    onUiEvent(BloodDonationRequestListUiEvent.MarkAsCompleteClicked(it))
                },
                onClickDonate = {
                    onUiEvent(BloodDonationRequestListUiEvent.DonateClicked(it))
                },
            )
        }
        if (isLoading) {
            item {
                BloodDonationRequestListItemShimmer()
            }
        }
    }
}

@Composable
private fun BloodDonationRequestListShimmer() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(6) {
            BloodDonationRequestListItemShimmer()
        }
    }
}

// Same approach as students list shimmer: Column + clip (no ElevatedCard surface) so the
// gradient reads correctly on the scaffold background in light mode.
@Composable
private fun BloodDonationRequestListItemShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .shimmerAnimation(),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(15.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .shimmerAnimation(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .shimmerAnimation(),
                )
            }
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .shimmerAnimation(),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerAnimation(),
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerAnimation(),
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RequestListItemView(
    item: BloodDonationRequestDisplayData,
    onClickItem: (BloodDonationRequestDisplayData) -> Unit,
    onClickCall: (String) -> Unit,
    onClickMarkAsComplete: (BloodDonationRequestDisplayData) -> Unit,
    onClickDonate: (BloodDonationRequestDisplayData) -> Unit,
) {
    val cardShape = RoundedCornerShape(16.dp)
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .clickable { onClickItem(item) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = cardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                LoadAsyncUserImage(
                    url = item.bloodDonationRequest.imageUrl,
                    size = 52.dp,
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = item.bloodDonationRequest.name,
                        style = TextStyle.body1.copy(
                            color = AppColors.textPrimary,
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )
                    BloodDonationRequestStatusRow(
                        isConfirmed = item.bloodDonationRequest.confirmed,
                        isCompleted = item.bloodDonationRequest.completed,
                        isOwnItem = item.isOwnItem,
                        onClickMarkAsComplete = { onClickMarkAsComplete(item) },
                        onClickDonate = { onClickDonate(item) },
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("Need ")
                            withStyle(
                                SpanStyle(
                                    color = AppColors.crimson,
                                    fontWeight = FontWeight.Bold,
                                ),
                            ) {
                                append(item.bloodDonationRequest.bloodGroup)
                            }
                            append(" blood · ")
                            withStyle(
                                SpanStyle(
                                    color = AppColors.textSecondary,
                                    fontWeight = FontWeight.Normal,
                                ),
                            ) {
                                append("before ")
                            }
                            withStyle(
                                SpanStyle(
                                    color = AppColors.textSecondary,
                                    fontWeight = FontWeight.SemiBold,
                                ),
                            ) {
                                append(item.needBeforeFormattedDate)
                            }
                        },
                        style = TextStyle.body2.copy(color = AppColors.textPrimary),
                    )
                    item.bloodDonationRequest.info?.let { info ->
                        Text(
                            text = info,
                            style = TextStyle.body3.copy(color = AppColors.textSecondary),
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }

            if (item.contacts.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                    ) {
                        Text(
                            text = "Request ID #${item.bloodDonationRequest.id}",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            style = TextStyle.label2.copy(MaterialTheme.colorScheme.onSurfaceVariant),
                        )
                    }
                    item.contacts.forEach { raw ->
                        BloodDonationContactRow(
                            rawNumber = raw,
                            onClick = { onClickCall(raw) },
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BloodDonationRequestStatusRow(
    isConfirmed: Boolean,
    isCompleted: Boolean,
    isOwnItem: Boolean,
    onClickMarkAsComplete: () -> Unit,
    onClickDonate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shouldShowMarkAsCompleteAction = isOwnItem && isConfirmed && !isCompleted
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        if (!isConfirmed) {
            BloodDonationRequestStatusBadge(
                text = stringResource(Res.string.blood_donation_request_status_pending),
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (shouldShowMarkAsCompleteAction) {
            Surface(
                modifier = Modifier.clickable(onClick = onClickMarkAsComplete),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Text(
                    text = stringResource(Res.string.blood_donation_request_mark_as_complete),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = TextStyle.label2.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            }
        } else {
            if (isConfirmed) {
                BloodDonationRequestStatusBadge(
                    text = stringResource(Res.string.blood_donation_request_status_active),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (isConfirmed && !isCompleted) {
            Surface(
                modifier = Modifier.clickable(onClick = onClickDonate),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Text(
                    text = stringResource(Res.string.txt_donate),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = TextStyle.label2.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            }
        }
    }
}

@Composable
private fun BloodDonationRequestStatusBadge(
    text: String,
    containerColor: Color,
    contentColor: Color,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = containerColor,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = TextStyle.label2.copy(
                color = contentColor,
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}

@Composable
private fun BloodDonationContactRow(
    rawNumber: String,
    onClick: () -> Unit,
) {
    val display = remember(rawNumber) { formatPhoneForDisplay(rawNumber) }
    Surface(
        modifier = Modifier.clip(RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = stringResource(Res.string.txt_call),
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = display,
                style = TextStyle.label2.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
    }
}

private fun formatPhoneForDisplay(raw: String): String {
    val trimmed = raw.trim()
    val digits = trimmed.filter { it.isDigit() }
    return when (digits.length) {
        11 if digits.startsWith("0") ->
            "${digits.substring(0, 4)} ${digits.substring(4, 7)} ${digits.substring(7)}"
        13 if digits.startsWith("880") ->
            "+880 ${digits.substring(3, 6)} ${digits.substring(6, 9)} ${digits.substring(9)}"
        10 ->
            "${digits.substring(0, 3)} ${digits.substring(3, 6)} ${digits.substring(6)}"
        else -> trimmed
    }
}
