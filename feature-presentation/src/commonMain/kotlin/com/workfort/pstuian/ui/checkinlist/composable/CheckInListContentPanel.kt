package com.workfort.pstuian.ui.checkinlist.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiEvent
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiState
import com.workfort.pstuian.ui.common.composable.AnimatedEmptyView
import com.workfort.pstuian.ui.common.composable.DotView
import com.workfort.pstuian.ui.common.composable.LoadAsyncImage
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.common.composable.shimmerAnimation
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.img_placeholder_profile
import androidx.compose.foundation.lazy.grid.items as gridItems

@Composable
internal fun CheckInListFullScreenShimmer(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        CheckInLocationListShimmer()
        CheckInListGridShimmer(modifier = Modifier.fillMaxWidth().weight(1f))
    }
}

@Composable
internal fun CheckInListContentPanel(
    modifier: Modifier = Modifier,
    uiState: CheckInListUiState.Content,
    onUiEvent: (CheckInListUiEvent) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        CheckInListHeaderView(
            checkInLocations = uiState.checkInLocations,
            selectedCheckInLocationId = uiState.selectedCheckInLocationId,
            isLoadingMore = uiState.isLocationListLoading && uiState.checkInLocations.isNotEmpty(),
            onClickLocation = { locationId ->
                onUiEvent(CheckInListUiEvent.OnSelectLocation(locationId))
            },
            onLoadMoreLocations = { onUiEvent(CheckInListUiEvent.OnLoadMoreLocations) },
        )

        when {
            uiState.checkInList.isEmpty() && uiState.isCheckInListLoading -> {
                CheckInListGridShimmer(modifier = Modifier.fillMaxWidth().weight(1f))
            }
            uiState.checkInList.isEmpty() -> {
                CheckInListCenterAction(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    onClick = { onUiEvent(CheckInListUiEvent.OnClickCheckIn) },
                    content = { AnimatedEmptyView() },
                )
            }
            else -> {
                CheckInListScrollableGrid(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    checkInList = uiState.checkInList,
                    currentUserId = uiState.currentUserId,
                    isContentLoading = uiState.isCheckInListLoading,
                    onUiEvent = onUiEvent,
                )
            }
        }
    }
}

@Composable
private fun CheckInLocationListShimmer(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(4) {
            Box(
                modifier = Modifier
                    .height(36.dp)
                    .width(if (it == 0) 96.dp else 80.dp)
                    .clip(CircleShape)
                    .shimmerAnimation(),
            )
        }
    }
}

@Composable
private fun CheckInListGridShimmer(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        gridItems(List(6) { it }) { _ ->
            CheckInListItemShimmer()
        }
    }
}

@Composable
private fun CheckInListItemShimmer() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .shimmerAnimation(),
        )
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerAnimation(),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerAnimation(),
            )
        }
    }
}

@Composable
private fun CheckInListScrollableGrid(
    modifier: Modifier,
    checkInList: List<CheckIn>,
    currentUserId: Int?,
    isContentLoading: Boolean,
    onUiEvent: (CheckInListUiEvent) -> Unit,
) {
    val listState = rememberLazyGridState()
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

    LaunchedEffect(shouldLoadMore, isContentLoading, checkInList.size) {
        val canRequestMore =
            shouldLoadMore && !isContentLoading && checkInList.isNotEmpty()
        if (canRequestMore && lastLoadMoreRequestedAtSize != checkInList.size) {
            lastLoadMoreRequestedAtSize = checkInList.size
            onUiEvent(CheckInListUiEvent.OnLoadMore)
        }
    }

    CheckInListView(
        modifier = modifier,
        checkInList = checkInList,
        currentUserId = currentUserId,
        listState = listState,
        isLoadingMore = isContentLoading && checkInList.isNotEmpty(),
        onClickItem = { onUiEvent(CheckInListUiEvent.OnClickItem(it)) },
        onClickCall = { onUiEvent(CheckInListUiEvent.OnClickCall(it)) },
        onClickCheckInSelf = { onUiEvent(CheckInListUiEvent.OnClickCheckIn) },
    )
}

@Composable
private fun CheckInListView(
    modifier: Modifier,
    checkInList: List<CheckIn>,
    currentUserId: Int?,
    listState: LazyGridState,
    isLoadingMore: Boolean,
    onClickItem: (CheckIn) -> Unit,
    onClickCall: (String) -> Unit,
    onClickCheckInSelf: () -> Unit,
) {
    val currentUserCheckIn = currentUserId?.let { userId ->
        checkInList.firstOrNull { it.userId == userId }
    }
    val otherCheckIns = currentUserCheckIn?.let { current ->
        checkInList.filterNot { it.id == current.id }
    } ?: checkInList

    LazyVerticalGrid(
        modifier = modifier,
        state = listState,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        currentUserCheckIn?.let { currentUserItem ->
            gridItems(
                items = listOf(currentUserItem),
                span = { GridItemSpan(maxLineSpan) },
            ) {
                CheckInListItemView(
                    item = currentUserItem,
                    onClickItem = { onClickItem(currentUserItem) },
                    onClickCall = { currentUserItem.phone?.let(onClickCall) },
                )
            }
        } ?: run {
            if (currentUserId != null) {
                gridItems(
                    items = listOf(Unit),
                    span = { GridItemSpan(maxLineSpan) },
                ) {
                    CheckInSelfActionCard(onClick = onClickCheckInSelf)
                }
            }
        }
        gridItems(otherCheckIns) { item ->
            CheckInListItemView(
                item = item,
                onClickItem = { onClickItem(item) },
                onClickCall = { item.phone?.let(onClickCall) },
            )
        }
        if (isLoadingMore) {
            gridItems(List(2) { it }) { _ ->
                CheckInListItemShimmer()
            }
        }
    }
}

@Composable
private fun CheckInSelfActionCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Check In Icon",
            )
            Text(
                text = "Check in now",
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun CheckInListItemView(
    item: CheckIn,
    onClickItem: () -> Unit,
    onClickCall: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickItem() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box {
                LoadAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    url = item.imageUrl,
                    placeholder = Res.drawable.img_placeholder_profile,
                    contentScale = ContentScale.Crop,
                )
                DotView(modifier = Modifier.padding(16.dp))
                item.phone?.let {
                    IconButton(
                        modifier = Modifier.align(Alignment.BottomStart),
                        onClick = { onClickCall() },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.White.copy(alpha = 0.2f),
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call Icon",
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                TitleTextSmall(text = item.name, fontSize = 16.sp)
                Text(text = item.batch, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun CheckInListHeaderView(
    checkInLocations: List<CheckInLocation>,
    selectedCheckInLocationId: Int,
    isLoadingMore: Boolean,
    onClickLocation: (Int) -> Unit,
    onLoadMoreLocations: () -> Unit,
) {
    val listState = rememberLazyListState()
    var didInitialScrollToSelected by remember { mutableStateOf(false) }
    var lastLoadMoreRequestedAtSize by remember { mutableIntStateOf(-1) }
    val shouldLoadMoreLocations by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            if (totalItems == 0) return@derivedStateOf false
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
            lastVisibleIndex >= totalItems - 1
        }
    }

    LaunchedEffect(shouldLoadMoreLocations, isLoadingMore, checkInLocations.size) {
        val canRequestMore = shouldLoadMoreLocations && !isLoadingMore && checkInLocations.isNotEmpty()
        if (canRequestMore && lastLoadMoreRequestedAtSize != checkInLocations.size) {
            lastLoadMoreRequestedAtSize = checkInLocations.size
            onLoadMoreLocations()
        }
    }
    LaunchedEffect(checkInLocations, selectedCheckInLocationId) {
        if (didInitialScrollToSelected) return@LaunchedEffect
        val selectedIndex = checkInLocations.indexOfFirst { it.id == selectedCheckInLocationId }
        if (selectedIndex >= 0) {
            listState.scrollToItem(selectedIndex)
            didInitialScrollToSelected = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (checkInLocations.isEmpty() && isLoadingMore) {
            CheckInLocationListShimmer()
            return@Column
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(checkInLocations) { location ->
                FilterChip(
                    selected = location.id == selectedCheckInLocationId,
                    shape = CircleShape,
                    onClick = {
                        if (location.id != selectedCheckInLocationId) {
                            onClickLocation(location.id)
                        }
                    },
                    label = { Text(location.name) },
                )
            }
            if (isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .height(30.dp)
                            .width(72.dp)
                            .clip(CircleShape)
                            .shimmerAnimation(),
                    )
                }
            }
        }
    }
}

@Composable
internal fun CheckInListCenterAction(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
        Button(
            modifier = Modifier.padding(top = 12.dp),
            onClick = onClick,
        ) {
            Text(text = "Check In")
        }
    }
}
