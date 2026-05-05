package com.workfort.pstuian.ui.checkinhistory.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.CheckInPrivacy
import com.workfort.pstuian.ui.common.composable.ActionButton
import com.workfort.pstuian.ui.common.composable.AnimatedEmptyView
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.OutlinedActionButton
import com.workfort.pstuian.ui.common.composable.ToggleSwitch
import com.workfort.pstuian.ui.common.composable.shimmerAnimation
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import com.workfort.pstuian.ui.checkinhistory.state.CheckInHistoryUiEvent
import com.workfort.pstuian.ui.checkinhistory.state.CheckInHistoryUiState
import com.workfort.pstuian.util.DateTimeUtilImpl
import com.workfort.pstuian.util.helper.MathUtil
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_change_privacy
import pstuian.feature_presentation.generated.resources.txt_delete
import pstuian.feature_presentation.generated.resources.txt_dismiss
import pstuian.feature_presentation.generated.resources.txt_only_me
import pstuian.feature_presentation.generated.resources.txt_public

/** Fixed square slot for list thumbnails (same size every row; vertically centered in the card). */
private val MyCheckInLocationListThumbnailSlotSize = 96.dp

private val MyCheckInBottomSheetThumbnailShape = RoundedCornerShape(20.dp)

@Composable
internal fun CheckInHistoryContentPanel(
    uiState: CheckInHistoryUiState.Content,
    onUiEvent: (CheckInHistoryUiEvent) -> Unit,
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

    Column(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.checkIns.isEmpty() && uiState.isContentLoading -> {
                CheckInHistoryShimmer()
            }
            uiState.checkIns.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedEmptyView()
                }
            }
            else -> {
                CheckInView(
                    checkIns = uiState.checkIns,
                    isLoading = uiState.isContentLoading,
                    onUiEvent = onUiEvent,
                )
            }
        }
    }
}

@Composable
private fun CheckInHistoryShimmer() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(6) {
            CheckInHistoryItemShimmer()
        }
    }
}

@Composable
private fun CheckInHistoryItemShimmer() {
    val cardShape = RoundedCornerShape(16.dp)
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = cardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(MyCheckInLocationListThumbnailSlotSize)
                    .clip(RectangleShape)
                    .shimmerAnimation(),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp, top = 10.dp, end = 14.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.72f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .shimmerAnimation(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.38f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .shimmerAnimation(),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(26.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .shimmerAnimation(),
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .shimmerAnimation(),
                    )
                }
            }
        }
    }
}

@Composable
private fun CheckInView(
    checkIns: List<CheckIn>,
    isLoading: Boolean,
    onUiEvent: (CheckInHistoryUiEvent) -> Unit,
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

    LaunchedEffect(shouldLoadMore, isLoading, checkIns.size) {
        val canRequestMore = shouldLoadMore && !isLoading && checkIns.isNotEmpty()
        if (canRequestMore && lastLoadMoreRequestedAtSize != checkIns.size) {
            lastLoadMoreRequestedAtSize = checkIns.size
            onUiEvent(CheckInHistoryUiEvent.LoadMore)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(checkIns) { item ->
            ListItemView(
                item = item,
                onClick = {
                    onUiEvent(CheckInHistoryUiEvent.ItemClicked(item))
                },
            )
        }
        if (isLoading) {
            item {
                CheckInHistoryItemShimmer()
            }
        }
    }
}

@Composable
private fun MyCheckInLocationThumbnailPlaceholderIcon() {
    Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription = null,
        modifier = Modifier.size(22.dp),
        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.62f),
    )
}

@Composable
private fun MyCheckInLocationThumbnail(
    imageUrl: String,
    modifier: Modifier = Modifier,
    containerShape: Shape = RectangleShape,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.Transparent,
) {
    val withBackground = modifier
        .clip(containerShape)
        .background(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            shape = containerShape,
        )
    val withBorder = if (borderWidth > 0.dp) {
        withBackground.border(
            width = borderWidth,
            color = borderColor,
            shape = containerShape,
        )
    } else {
        withBackground
    }
    Box(
        modifier = withBorder,
        contentAlignment = Alignment.Center,
    ) {
        if (imageUrl.isBlank()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                MyCheckInLocationThumbnailPlaceholderIcon()
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
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(containerShape),
                        contentScale = ContentScale.Crop,
                    )
                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        MyCheckInLocationThumbnailPlaceholderIcon()
                    }
                }
            }
        }
    }
}

@Composable
private fun ListItemView(
    item: CheckIn,
    onClick: () -> Unit,
) {
    val dateTimeUtil = DateTimeUtilImpl()
    val date = dateTimeUtil.getTimeAgo(item.date)
    val checkInCountStr = "${MathUtil.prettyCount(item.count)} check in"
    val privacyTxt = when (item.privacy) {
        CheckInPrivacy.ONLY_ME.value -> stringResource(Res.string.txt_only_me)
        else -> stringResource(Res.string.txt_public)
    }
    val cardShape = RoundedCornerShape(16.dp)
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .clickable(onClick = onClick),
        shape = cardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MyCheckInLocationThumbnail(
                imageUrl = item.locationImageUrl.orEmpty(),
                modifier = Modifier.size(MyCheckInLocationListThumbnailSlotSize),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp, top = 10.dp, end = 14.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = item.locationName,
                    style = TextStyle.body2.copy(
                        color = AppColors.textPrimary,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = date,
                    style = TextStyle.label2.copy(color = AppColors.textSecondary),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                    ) {
                        Text(
                            text = privacyTxt,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            style = TextStyle.label2.copy(
                                color = AppColors.textSecondary,
                                fontWeight = FontWeight.Medium,
                            ),
                        )
                    }
                    Text(
                        text = "·",
                        style = TextStyle.label2.copy(color = AppColors.textTertiary),
                    )
                    Text(
                        text = checkInCountStr,
                        style = TextStyle.label2.copy(color = AppColors.textSecondary),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCheckInItemBottomSheet(
    item: CheckIn,
    onClickChangePrivacy: (CheckInPrivacy) -> Unit,
    onClickDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    val detailsDialogSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val dateTimeUtil = DateTimeUtilImpl()
    val date = dateTimeUtil.getTimeAgo(item.date)
    val checkInCountStr = "${MathUtil.prettyCount(item.count)} check in"
    val privacyOptions = listOf(
        stringResource(Res.string.txt_public),
        stringResource(Res.string.txt_only_me),
    )
    val committedPrivacyIndex = when (CheckInPrivacy.create(item.privacy)) {
        CheckInPrivacy.ONLY_ME -> 1
        else -> 0
    }
    var selectedPrivacyIndex by remember(item.id, item.privacy) {
        mutableIntStateOf(committedPrivacyIndex)
    }

    fun hideThen(run: () -> Unit) {
        scope.launch { detailsDialogSheetState.hide() }.invokeOnCompletion {
            run()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = detailsDialogSheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 8.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MyCheckInLocationThumbnail(
                imageUrl = item.locationImageUrl.orEmpty(),
                modifier = Modifier.size(96.dp),
                containerShape = MyCheckInBottomSheetThumbnailShape,
                borderWidth = 1.dp,
                borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.locationName,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = TextStyle.title3.copy(
                    color = AppColors.textPrimary,
                    fontWeight = FontWeight.SemiBold,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "$date · $checkInCountStr",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = TextStyle.label2.copy(color = AppColors.textSecondary),
            )

            Spacer(modifier = Modifier.height(24.dp))

            ToggleSwitch(
                modifier = Modifier.fillMaxWidth(),
                options = privacyOptions,
                selectedIndex = selectedPrivacyIndex,
                onSelectedIndexChange = { selectedPrivacyIndex = it },
                height = 44.dp,
                cornerRadius = 22.dp,
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 20.dp),
                color = AppColors.divider,
            )

            val privacyDirty = committedPrivacyIndex != selectedPrivacyIndex
            ActionButton(
                label = stringResource(Res.string.txt_change_privacy),
                icon = null,
                enabled = privacyDirty,
                onClick = {
                    hideThen {
                        val privacy = when (selectedPrivacyIndex) {
                            1 -> CheckInPrivacy.ONLY_ME
                            else -> CheckInPrivacy.PUBLIC
                        }
                        onClickChangePrivacy(privacy)
                    }
                },
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedActionButton(
                label = stringResource(Res.string.txt_delete),
                icon = Icons.Default.Delete,
                borderColor = AppColors.error,
                contentColor = AppColors.error,
                onClick = {
                    hideThen(onClickDelete)
                },
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = { hideThen(onDismiss) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(Res.string.txt_dismiss),
                    style = TextStyle.label1.copy(color = AppColors.textSecondary),
                )
            }
        }
    }
}
