package com.workfort.pstuian.ui.checkinlist.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
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
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.ui.checkinlist.displaydata.CheckInDisplayData
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiEvent
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiState
import com.workfort.pstuian.ui.common.composable.LoadAsyncUserImage
import com.workfort.pstuian.ui.common.composable.OnlineOfflineStatusLabel
import com.workfort.pstuian.ui.common.composable.shimmerAnimation
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import kotlinx.coroutines.delay
import androidx.compose.foundation.lazy.grid.items as gridItems

private const val GridColumnCount = 3
private val CheckInCardContentPadding = 8.dp
private val CheckInCardTextPadding = PaddingValues(
    start = CheckInCardContentPadding,
    top = 0.dp,
    end = CheckInCardContentPadding,
    bottom = CheckInCardContentPadding,
)
private val CheckInAvatarOuterSize = 80.dp

private val OfflineStatusLightGray = Color(0xFFD6D6D6)

private const val CheckInNameMarqueeMillis = 2400
private const val CheckInNameMarqueePauseMillis = 400

private val CheckInListCardShape = RoundedCornerShape(12.dp)
/** Square location thumbnail (width == height); row height matches so the slot is not clipped. */
private val CheckInLocationListThumbnailSize = 48.dp
private val CheckInLocationListRowHeight = CheckInLocationListThumbnailSize
private val CheckInListGridSpacing = 16.dp

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
            .padding(
                horizontal = CheckInListGridSpacing,
                vertical = CheckInListGridSpacing,
            ),
        horizontalArrangement = Arrangement.spacedBy(CheckInListGridSpacing),
    ) {
        repeat(4) {
            Box(
                modifier = Modifier
                    .height(CheckInLocationListRowHeight)
                    .width(168.dp)
                    .clip(CheckInListCardShape)
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
        contentPadding = PaddingValues(horizontal = CheckInListGridSpacing),
        horizontalArrangement = Arrangement.spacedBy(CheckInListGridSpacing),
        verticalArrangement = Arrangement.spacedBy(CheckInListGridSpacing),
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
                    .size(CheckInAvatarOuterSize)
                    .clip(CircleShape)
                    .shimmerAnimation(),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CheckInCardTextPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(11.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerAnimation(),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .height(9.dp)
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
        contentPadding = PaddingValues(horizontal = CheckInListGridSpacing),
        horizontalArrangement = Arrangement.spacedBy(CheckInListGridSpacing),
        verticalArrangement = Arrangement.spacedBy(CheckInListGridSpacing),
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
            .clip(CheckInListCardShape)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = CheckInListCardShape,
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
            .clip(CheckInListCardShape)
            .clickable { onClickItem() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = CheckInListCardShape,
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
private fun LocationThumbnailPlaceholderIcon() {
    Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription = null,
        modifier = Modifier.size(22.dp),
        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.62f),
    )
}

@Composable
private fun CheckInLocationListThumbnail(
    imageUrl: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val slotBackground = if (selected) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    } else {
        MaterialTheme.colorScheme.surfaceContainerLow
    }
    Box(
        modifier = modifier
            .clip(RectangleShape)
            .background(slotBackground),
        contentAlignment = Alignment.Center,
    ) {
        if (imageUrl.isBlank()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                LocationThumbnailPlaceholderIcon()
            }
        } else {
            val platformContext = LocalPlatformContext.current
            val request = remember(imageUrl, platformContext) {
                ImageRequest.Builder(platformContext)
                    .data(imageUrl)
                    .crossfade(true)
                    .build()
            }
            val painter = rememberAsyncImagePainter(
                model = request,
                contentScale = ContentScale.Crop,
            )
            val state by painter.state.collectAsState()
            when (state) {
                is AsyncImagePainter.State.Success -> {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        LocationThumbnailPlaceholderIcon()
                    }
                }
            }
        }
    }
}

@Composable
private fun CheckInLocationListItemCard(
    location: CheckInLocation,
    selected: Boolean,
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .wrapContentWidth()
            .clip(CheckInListCardShape)
            .clickable(enabled = !selected, onClick = onClick),
        shape = CheckInListCardShape,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .height(CheckInLocationListRowHeight),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CheckInLocationListThumbnail(
                imageUrl = location.imageUrl.orEmpty(),
                selected = selected,
                modifier = Modifier.size(CheckInLocationListThumbnailSize),
            )
            Column(
                modifier = Modifier
                    .widthIn(max = 200.dp)
                    .padding(top = 4.dp, bottom = 4.dp, start = 10.dp, end = 12.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = location.name,
                    style = TextStyle.label3.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.textPrimary,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${location.count} check in",
                    style = TextStyle.label3.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 9.sp,
                        lineHeight = 12.sp,
                        color = AppColors.textSecondary,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
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
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (checkInLocations.isEmpty() && isLoadingMore) {
            CheckInLocationListShimmer()
            return@Column
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            contentPadding = PaddingValues(
                horizontal = CheckInListGridSpacing,
                vertical = CheckInListGridSpacing,
            ),
            horizontalArrangement = Arrangement.spacedBy(CheckInListGridSpacing),
        ) {
            items(checkInLocations) { location ->
                val selected = location.id == selectedCheckInLocationId
                CheckInLocationListItemCard(
                    location = location,
                    selected = selected,
                    onClick = { onClickLocation(location.id) },
                )
            }
            if (isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .height(CheckInLocationListRowHeight)
                            .width(168.dp)
                            .clip(CheckInListCardShape)
                            .shimmerAnimation(),
                    )
                }
            }
        }
    }
}
