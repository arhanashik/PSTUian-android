package com.workfort.pstuian.ui.donation.donors.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.Donor
import com.workfort.pstuian.ui.common.composable.AnimatedEmptyView
import com.workfort.pstuian.ui.common.composable.LabelText
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.common.composable.shimmerAnimation
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import com.workfort.pstuian.ui.donation.donors.state.DonorsUiEvent
import com.workfort.pstuian.ui.donation.donors.state.DonorsUiState

@Composable
fun DonorsContentPanel(
    uiState: DonorsUiState.Content,
    onUiEvent: (DonorsUiEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (uiState.donors.isEmpty() && uiState.isLoading) {
            DonorsListShimmer()
        } else if (uiState.donors.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedEmptyView()
            }
        } else {
            DonorListView(
                donorList = uiState.donors,
                isLoading = uiState.isLoading,
                onUiEvent = onUiEvent,
            )
        }
    }
}

@Composable
private fun DonorListView(
    donorList: List<Donor>,
    isLoading: Boolean,
    onUiEvent: (DonorsUiEvent) -> Unit,
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

    LaunchedEffect(shouldLoadMore, isLoading, donorList.size) {
        val canRequestMore = shouldLoadMore && !isLoading && donorList.isNotEmpty()
        if (canRequestMore && lastLoadMoreRequestedAtSize != donorList.size) {
            lastLoadMoreRequestedAtSize = donorList.size
            onUiEvent(DonorsUiEvent.LoadMore)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(donorList) { item ->
            DonorListItemView(
                item = item,
                onClickItem = {
                    onUiEvent(DonorsUiEvent.DonorClicked(it))
                },
            )
        }
        if (isLoading) {
            item {
                DonorListItemShimmer()
            }
        }
    }
}

@Composable
internal fun DonorsListShimmer() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(6) {
            DonorListItemShimmer()
        }
    }
}

@Composable
private fun DonorListItemShimmer() {
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
                        .fillMaxWidth()
                        .height(15.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .shimmerAnimation(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .shimmerAnimation(),
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmerAnimation(),
        )
    }
}

@Composable
private fun DonorListItemView(
    item: Donor,
    onClickItem: (Donor) -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClickItem(item) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
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
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(10.dp),
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = item.name ?: "Anonymous",
                        style = TextStyle.title3.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    )
                    Text(
                        text = item.email ?: "No email",
                        style = TextStyle.body2.copy(color = AppColors.textSecondary),
                    )
                }
            }

            item.info?.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = it,
                    style = TextStyle.body2.copy(color = AppColors.textPrimary),
                )
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Reference",
                        style = TextStyle.label2.copy(color = AppColors.textSecondary),
                    )
                    Text(
                        text = item.reference,
                        style = TextStyle.label2.copy(color = AppColors.textSecondary),
                    )
                }
            }
        }
    }
}
