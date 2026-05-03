package com.workfort.pstuian.ui.mycheckinlist.composable

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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.CheckInPrivacy
import com.workfort.pstuian.ui.common.composable.AnimatedEmptyView
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.DotView
import com.workfort.pstuian.ui.common.composable.LoadAsyncImage
import com.workfort.pstuian.ui.common.composable.MaterialButtonToggleGroup
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.common.composable.shimmerAnimation
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInListUiEvent
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInListUiState
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

@Composable
fun MyCheckInListContentPanel(
    uiState: MyCheckInListUiState.Content,
    onUiEvent: (MyCheckInListUiEvent) -> Unit,
) {
    if (uiState.error != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedErrorView()
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.checkIns.isEmpty() && uiState.isContentLoading -> {
                MyCheckInListShimmer(modifier = Modifier.fillMaxSize())
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
                CheckInListView(
                    checkIns = uiState.checkIns,
                    isLoading = uiState.isContentLoading,
                    onUiEvent = onUiEvent,
                )
            }
        }
    }
}

@Composable
private fun MyCheckInListShimmer(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(6) {
            MyCheckInListItemShimmer()
        }
    }
}

@Composable
private fun MyCheckInListItemShimmer() {
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(width = 72.dp, height = 76.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerAnimation(),
            )
            Column(
                modifier = Modifier.weight(1f),
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
private fun CheckInListView(
    checkIns: List<CheckIn>,
    isLoading: Boolean,
    onUiEvent: (MyCheckInListUiEvent) -> Unit,
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
            onUiEvent(MyCheckInListUiEvent.LoadMore)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(checkIns) { item ->
            ListItemView(
                item = item,
                onClick = {
                    onUiEvent(MyCheckInListUiEvent.ItemClicked(item))
                },
            )
        }
        if (isLoading) {
            item {
                MyCheckInListItemShimmer()
            }
        }
    }
}

@Composable
private fun MyCheckInLocationThumbnail(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = modifier
            .size(width = 72.dp, height = 76.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                shape = shape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        LoadAsyncImage(
            modifier = Modifier.fillMaxSize(),
            url = imageUrl,
            placeholder = Icons.Default.LocationOn,
            contentScale = ContentScale.Crop,
        )
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            MyCheckInLocationThumbnail(imageUrl = item.locationImageUrl.orEmpty())
            Column(
                modifier = Modifier.weight(1f),
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
    val detailsDialogSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val dateTimeUtil = DateTimeUtilImpl()
    val date = dateTimeUtil.getTimeAgo(item.date)
    val checkInCountStr = "${MathUtil.prettyCount(item.count)} check in"
    val privacyItems = listOf(
        stringResource(Res.string.txt_public),
        stringResource(Res.string.txt_only_me),
    )
    val privacyIndex = when(CheckInPrivacy.create(item.privacy)) {
        null, CheckInPrivacy.PUBLIC -> 0
        CheckInPrivacy.ONLY_ME -> 1
    }
    val (selectedPrivacyIndex, onChangePrivacyIndex) = remember { mutableIntStateOf(privacyIndex) }
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = detailsDialogSheetState,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LoadAsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                url = item.locationImageUrl,
                placeholder = Icons.Default.LocationOn,
                contentScale = ContentScale.Crop,
            )
            TitleTextSmall(modifier = Modifier.padding(top = 8.dp), text = item.name)
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = date)
                DotView(modifier = Modifier.padding(horizontal = 8.dp), color = Color.Gray)
                Text(text = checkInCountStr)
            }
            MaterialButtonToggleGroup(
                items = privacyItems,
                selectedIndex = selectedPrivacyIndex,
            ) {
                onChangePrivacyIndex(it)
            }
            HorizontalDivider(
                modifier = Modifier.padding(top = 16.dp),
                color = Color.LightGray.copy(alpha = 0.5f),
            )
            TextButton(
                onClick = {
                    scope.launch { detailsDialogSheetState.hide() }.invokeOnCompletion {
                        val privacy = when (selectedPrivacyIndex) {
                            0 -> CheckInPrivacy.PUBLIC
                            1 -> CheckInPrivacy.ONLY_ME
                            else -> CheckInPrivacy.PUBLIC
                        }
                        onClickChangePrivacy(privacy)
                    }
                },
                enabled = privacyIndex != selectedPrivacyIndex,
            ) {
                Text(text = stringResource(Res.string.txt_change_privacy))
            }
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            TextButton(
                onClick = {
                    scope.launch { detailsDialogSheetState.hide() }.invokeOnCompletion {
                        onClickDelete()
                    }
                }
            ) {
                Text(text = stringResource(Res.string.txt_delete))
            }
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            TextButton(
                onClick = {
                    scope.launch { detailsDialogSheetState.hide() }.invokeOnCompletion {
                        onDismiss()
                    }
                }
            ) {
                Text(text = stringResource(Res.string.txt_dismiss))
            }
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
        }
    }
}
