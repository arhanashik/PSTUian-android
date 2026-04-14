package com.workfort.pstuian.ui.checkinlist.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.common.composable.AnimatedEmptyView
import com.workfort.pstuian.common.composable.DotView
import com.workfort.pstuian.common.composable.LoadAsyncImage
import com.workfort.pstuian.common.composable.TitleTextSmall
import com.workfort.pstuian.common.composable.isLastItemVisible
import com.workfort.pstuian.featuredomain.model.CheckInEntity
import com.workfort.pstuian.featuredomain.model.CheckInLocationEntity
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiEvent
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiState
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.img_placeholder_profile

@Composable
internal fun CheckInListContentPanel(
    modifier: Modifier = Modifier,
    uiState: CheckInListUiState.Content,
    onUiEvent: (CheckInListUiEvent) -> Unit,
) {
    val listState = rememberLazyGridState()
    val isLastItemVisible by remember { derivedStateOf { listState.isLastItemVisible } }

    LaunchedEffect(isLastItemVisible) {
        if (isLastItemVisible) {
            onUiEvent(CheckInListUiEvent.OnLoadMore)
        }
    }

    Column(modifier = modifier) {
        uiState.checkInLocation?.let { location ->
            CheckInListHeaderView(
                checkInLocation = location,
                onClickChangeLocation = {
                    onUiEvent(CheckInListUiEvent.OnClickChangeLocation)
                },
                onClickCheckIn = {
                    onUiEvent(CheckInListUiEvent.OnClickCheckIn)
                },
            )
        }

        if (uiState.checkInList.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    AnimatedEmptyView(modifier = Modifier.width(200.dp))
                }
            }
        } else {
            CheckInListView(
                modifier = Modifier.fillMaxWidth().weight(1f),
                checkInList = uiState.checkInList,
                listState = listState,
                isLoading = uiState.isLoading,
                onClickItem = { onUiEvent(CheckInListUiEvent.OnClickItem(it)) },
                onClickCall = { onUiEvent(CheckInListUiEvent.OnClickCall(it)) },
            )
        }
    }
}

@Composable
private fun CheckInListView(
    modifier: Modifier,
    checkInList: List<CheckInEntity>,
    listState: LazyGridState,
    isLoading: Boolean,
    onClickItem: (CheckInEntity) -> Unit,
    onClickCall: (String) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        state = listState,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(checkInList) { item ->
            CheckInListItemView(
                item = item,
                onClickItem = { onClickItem(item) },
                onClickCall = { item.phone?.let(onClickCall) }
            )
        }
        if (isLoading) {
            item { CircularProgressIndicator() }
        }
    }
}

@Composable
private fun CheckInListItemView(
    item: CheckInEntity,
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
        shape = RoundedCornerShape(8.dp),
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
    checkInLocation: CheckInLocationEntity,
    onClickChangeLocation: () -> Unit,
    onClickCheckIn: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 14.sp)) {
                append("Showing peoples in ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                append(checkInLocation.name)
            }
        }
        Text(text = annotatedString)
        Row {
            AssistChip(
                onClick = { onClickChangeLocation() },
                label = { Text("Change Location") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Change Location Icon",
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                shape = CircleShape,
            )
            Spacer(modifier = Modifier.padding(start = 8.dp))
            AssistChip(
                onClick = { onClickCheckIn() },
                label = { Text("Check In") },
                leadingIcon = {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Check In Icon",
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                shape = CircleShape,
            )
        }
    }
}
