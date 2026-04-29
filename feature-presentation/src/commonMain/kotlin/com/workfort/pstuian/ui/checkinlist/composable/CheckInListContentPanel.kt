package com.workfort.pstuian.ui.checkinlist.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.ui.checkinlist.displaydata.CheckInDisplayData
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiEvent
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiState
import com.workfort.pstuian.ui.common.composable.LoadAsyncUserImage
import com.workfort.pstuian.ui.common.composable.OnlineOfflineStatusLabel
import com.workfort.pstuian.ui.common.composable.shimmerAnimation
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import androidx.compose.foundation.lazy.grid.items as gridItems
import kotlinx.coroutines.delay

private val ScreenHorizontalPadding = 16.dp
private const val GridColumnCount = 3
private val SectionTopPadding = 8.dp
private val ChipRowVerticalPadding = 2.dp
private val CheckInCardContentPadding = 8.dp
private val CheckInCardTextPadding = PaddingValues(
    start = CheckInCardContentPadding,
    top = 0.dp,
    end = CheckInCardContentPadding,
    bottom = CheckInCardContentPadding,
)

private val OfflineStatusLightGray = Color(0xFFD6D6D6)

private const val CheckInNameMarqueeMillis = 2400
private const val CheckInNameMarqueePauseMillis = 400

@Composable
private fun CheckInNameSingleLineMaybeMarquee(
    name: String,
    modifier: Modifier = Modifier,
) {
    val textStyle = TextStyle.label3.copy(
        fontWeight = FontWeight.SemiBold,
        color = AppColors.textPrimary,
    )
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val textLayout = textMeasurer.measure(
        text = AnnotatedString(name),
        style = textStyle,
        overflow = TextOverflow.Clip,
        softWrap = false,
        maxLines = 1,
        constraints = Constraints(maxWidth = Constraints.Infinity),
    )
    val textHeightDp = with(density) { textLayout.size.height.toDp() }
    val textWidthPx = textLayout.size.width.toFloat()
    val anim = remember { Animatable(0f) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(textHeightDp),
    ) {
        val maxWidthPx = with(density) { maxWidth.toPx() }
        val overflowPx = if (maxWidthPx < 0.5f) {
            0f
        } else {
            (textWidthPx - maxWidthPx).coerceAtLeast(0f)
        }

        LaunchedEffect(name, maxWidthPx, overflowPx) {
            if (overflowPx <= 0f) {
                anim.snapTo(0f)
                return@LaunchedEffect
            }
            while (true) {
                anim.snapTo(0f)
                anim.animateTo(
                    targetValue = overflowPx,
                    animationSpec = tween(
                        durationMillis = CheckInNameMarqueeMillis,
                        easing = LinearEasing,
                    ),
                )
                anim.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = CheckInNameMarqueeMillis,
                        easing = LinearEasing,
                    ),
                )
                delay(CheckInNameMarqueePauseMillis.toLong())
            }
        }

        if (overflowPx <= 0f) {
            Text(
                text = name,
                style = textStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clipToBounds(),
            ) {
                Text(
                    text = name,
                    style = textStyle,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier.graphicsLayer { translationX = -anim.value },
                )
            }
        }
    }
}

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
        columns = GridCells.Fixed(GridColumnCount),
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
                .padding(CheckInCardContentPadding),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .shimmerAnimation(),
            )
        }
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerAnimation(),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .height(8.dp)
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
        onClickCheckInSelf = { onUiEvent(CheckInListUiEvent.CheckInClicked(selectedLocationId)) },
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
    onClickCheckInSelf: () -> Unit,
) {

    LazyVerticalGrid(
        modifier = modifier,
        state = listState,
        columns = GridCells.Fixed(GridColumnCount),
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
                )
            }
        }

        // other check in items
        gridItems(otherCheckIns) { item ->
            CheckInListItemView(
                item = item,
                onClickItem = { onClickItem(item) },
            )
        }

        // load more items
        if (isLoadingMore) {
            gridItems(List(GridColumnCount) { it }) { _ ->
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
                    .padding(CheckInCardContentPadding),
            ) {
                Box(
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape,
                            )
                            .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
                            .padding(4.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Check In Here Icon",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(34.dp),
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CheckInCardTextPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Check In Here",
                    style = TextStyle.label3.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.textPrimary,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = "Tap to check in",
                    style = TextStyle.label3.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 9.sp,
                        lineHeight = 12.sp,
                        color = AppColors.textSecondary,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun CheckInListItemView(
    item: CheckInDisplayData,
    onClickItem: () -> Unit,
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CheckInCardContentPadding),
            ) {
                val ringBorderColor = if (item.isOnline) {
                    MaterialTheme.colorScheme.primary
                } else {
                    OfflineStatusLightGray
                }
                Box(
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(width = 2.dp, color = ringBorderColor, shape = CircleShape)
                            .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
                            .padding(4.dp),
                    ) {
                        LoadAsyncUserImage(url = item.checkIn.imageUrl, size = 64.dp)
                    }
                }
                OnlineOfflineStatusLabel(
                    isOnline = item.isOnline,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .zIndex(1f),
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CheckInCardTextPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CheckInNameSingleLineMaybeMarquee(
                    name = item.checkIn.name,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = item.checkIn.batch,
                    style = TextStyle.label3.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 9.sp,
                        lineHeight = 12.sp,
                        color = AppColors.textSecondary,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
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
