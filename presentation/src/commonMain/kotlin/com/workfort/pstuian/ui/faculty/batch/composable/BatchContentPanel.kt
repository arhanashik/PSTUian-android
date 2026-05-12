package com.workfort.pstuian.ui.faculty.batch.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.ui.common.composable.AnimatedEmptyView
import com.workfort.pstuian.ui.common.composable.shimmerAnimation
import com.workfort.pstuian.ui.faculty.batch.state.BatchUiEvent
import com.workfort.pstuian.ui.faculty.batch.state.BatchUiState
import com.workfort.pstuian.ui.faculty.composable.BatchListItemView

@Composable
internal fun BatchContentPanel(
    uiState: BatchUiState.Content,
    onUiEvent: (BatchUiEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (uiState.batches.isEmpty() && uiState.isLoading) {
            BatchListShimmer()
            return
        }

        if (uiState.batches.isEmpty()) {
            BatchPullToRefreshBox(
                isContentLoading = uiState.isLoading,
                onRefresh = { onUiEvent(BatchUiEvent.Refresh) },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedEmptyView()
                }
            }
        } else {
            BatchListView(
                batches = uiState.batches,
                isContentLoading = uiState.isLoading,
                onUiEvent = onUiEvent,
            )
        }
    }
}

@Composable
private fun BatchPullToRefreshBox(
    isContentLoading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {
    var refreshFromPull by remember { mutableStateOf(false) }
    LaunchedEffect(isContentLoading) {
        if (!isContentLoading) refreshFromPull = false
    }
    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = isContentLoading && refreshFromPull,
        onRefresh = {
            refreshFromPull = true
            onRefresh()
        },
    ) {
        content()
    }
}

@Composable
private fun BatchListView(
    batches: List<Batch>,
    isContentLoading: Boolean,
    onUiEvent: (BatchUiEvent) -> Unit,
) {
    val listState = rememberLazyListState()
    var lastLoadMoreRequestedAtSize by remember { mutableIntStateOf(-1) }
    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            if (totalItems == 0) return@derivedStateOf false
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: return@derivedStateOf false
            lastVisibleIndex >= totalItems - 1
        }
    }

    LaunchedEffect(shouldLoadMore, isContentLoading, batches.size) {
        val canRequestMore = shouldLoadMore && !isContentLoading && batches.isNotEmpty()
        if (canRequestMore && lastLoadMoreRequestedAtSize != batches.size) {
            lastLoadMoreRequestedAtSize = batches.size
            onUiEvent(BatchUiEvent.LoadMore)
        }
    }

    BatchPullToRefreshBox(
        isContentLoading = isContentLoading,
        onRefresh = { onUiEvent(BatchUiEvent.Refresh) },
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(batches) { batch ->
                BatchListItemView(batch) { onUiEvent(BatchUiEvent.BatchClicked(batch)) }
            }
            if (isContentLoading) {
                item { BatchListItemShimmer() }
            }
        }
    }
}

@Composable
internal fun BatchListShimmer() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(6) {
            BatchListItemShimmer()
        }
    }
}

@Composable
private fun BatchListItemShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.56f)
                    .height(18.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .shimmerAnimation(),
            )
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(26.dp)
                    .clip(RoundedCornerShape(99.dp))
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
