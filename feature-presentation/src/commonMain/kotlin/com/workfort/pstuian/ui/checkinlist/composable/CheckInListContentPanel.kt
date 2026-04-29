package com.workfort.pstuian.ui.checkinlist.composable

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.ui.checkinlist.displaydata.CheckInDisplayData
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiEvent
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiState
import com.workfort.pstuian.ui.common.composable.LoadAsyncImage
import com.workfort.pstuian.ui.common.composable.OnlineOfflineStatusLabel
import com.workfort.pstuian.ui.common.composable.StatusPillLabelText
import com.workfort.pstuian.ui.common.composable.shimmerAnimation
import org.jetbrains.compose.resources.stringResource
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.img_placeholder_profile
import pstuian.feature_presentation.generated.resources.txt_call
import androidx.compose.foundation.lazy.grid.items as gridItems

private val ScreenHorizontalPadding = 16.dp
private val SectionTopPadding = 8.dp
private val ChipRowVerticalPadding = 2.dp

@Composable
internal fun CheckInListFullScreenShimmer(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        CheckInLocationListShimmer()
        CheckInListGridShimmer(modifier = Modifier.fillMaxSize())
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
            selectedCheckInLocationId = uiState.selectedLocationId,
            isLoadingMore = uiState.isLocationListLoading && uiState.checkInLocations.isNotEmpty(),
            onClickLocation = { locationId ->
                onUiEvent(CheckInListUiEvent.LocationSelected(locationId))
            },
            onLoadMoreLocations = { onUiEvent(CheckInListUiEvent.OnLoadMoreLocations) },
        )

        when {
            uiState.otherCheckIns.isEmpty() && uiState.isCheckInListLoading -> {
                CheckInListGridShimmer(modifier = Modifier.fillMaxSize())
            }
            else -> {
                CheckInListScrollableGrid(
                    modifier = Modifier.fillMaxSize(),
                    selectedLocationId = uiState.selectedLocationId,
                    currentUserCheckIn = uiState.currentUserCheckIn,
                    otherCheckIns = uiState.otherCheckIns,
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
            .padding(horizontal = ScreenHorizontalPadding, vertical = 8.dp),
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
        contentPadding = PaddingValues(ScreenHorizontalPadding),
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
    selectedLocationId: Int,
    currentUserCheckIn: CheckInDisplayData?,
    otherCheckIns: List<CheckInDisplayData>,
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

    LaunchedEffect(shouldLoadMore, isContentLoading, otherCheckIns.size) {
        val canRequestMore =
            shouldLoadMore && !isContentLoading && otherCheckIns.isNotEmpty()
        if (canRequestMore && lastLoadMoreRequestedAtSize != otherCheckIns.size) {
            lastLoadMoreRequestedAtSize = otherCheckIns.size
            onUiEvent(CheckInListUiEvent.OnLoadMoreCheckIn(selectedLocationId))
        }
    }

    CheckInListView(
        modifier = modifier,
        currentUserCheckIn = currentUserCheckIn,
        otherCheckIns = otherCheckIns,
        listState = listState,
        isLoadingMore = isContentLoading && otherCheckIns.isNotEmpty(),
        onClickItem = { onUiEvent(CheckInListUiEvent.CheckInItemClicked(it)) },
        onClickCall = { phoneNumber -> onUiEvent(CheckInListUiEvent.CallClicked(phoneNumber)) },
        onClickCheckInSelf = { onUiEvent(CheckInListUiEvent.CheckInClicked) },
    )
}

@Composable
private fun CheckInListView(
    modifier: Modifier,
    currentUserCheckIn: CheckInDisplayData?,
    otherCheckIns: List<CheckInDisplayData>,
    listState: LazyGridState,
    isLoadingMore: Boolean,
    onClickItem: (CheckInDisplayData) -> Unit,
    onClickCall: (String) -> Unit,
    onClickCheckInSelf: () -> Unit,
) {

    LazyVerticalGrid(
        modifier = modifier,
        state = listState,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(ScreenHorizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // current user check in item
        if (currentUserCheckIn == null) {
            gridItems(items = listOf(Unit)) {
                CheckInSelfActionCard(onClick = onClickCheckInSelf)
            }
        } else {
            gridItems(listOf(currentUserCheckIn)) { item ->
                CheckInListItemView(
                    item = item,
                    onClickItem = { onClickItem(item) },
                    onClickCall = onClickCall,
                )
            }
        }

        // other check in items
        gridItems(otherCheckIns) { item ->
            CheckInListItemView(
                item = item,
                onClickItem = { onClickItem(item) },
                onClickCall = onClickCall,
            )
        }

        // load more items
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Check In Here Icon",
                            tint = AppColors.primary,
                            modifier = Modifier.size(40.dp),
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Check In Here",
                    style = TextStyle.title3.copy(color = AppColors.textPrimary),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Tap to check in",
                    style = TextStyle.body2.copy(color = AppColors.textSecondary),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun CheckInListItemView(
    item: CheckInDisplayData,
    onClickItem: () -> Unit,
    onClickCall: (String) -> Unit,
) {
    val phoneNumber = item.checkIn.phone
    val canShowCallButton = !phoneNumber.isNullOrBlank()
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            ) {
                LoadAsyncImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    url = item.checkIn.imageUrl,
                    placeholder = Res.drawable.img_placeholder_profile,
                    contentScale = ContentScale.Crop,
                )
                OnlineOfflineStatusLabel(
                    isOnline = item.isOnline,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp),
                )

                if (canShowCallButton) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 6.dp, bottom = 4.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                            .clickable { onClickCall(phoneNumber) },
                        contentAlignment = Alignment.Center,
                    ) {
                        StatusPillLabelText(text = stringResource(Res.string.txt_call))
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = item.checkIn.name,
                    style = TextStyle.title3.copy(color = AppColors.textPrimary),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = item.checkIn.batch,
                    style = TextStyle.body2.copy(color = AppColors.textSecondary),
                    textAlign = TextAlign.Center,
                )
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
            .padding(top = SectionTopPadding),
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
                .padding(vertical = ChipRowVerticalPadding),
            state = listState,
            contentPadding = PaddingValues(horizontal = ScreenHorizontalPadding),
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
            .padding(horizontal = ScreenHorizontalPadding),
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
