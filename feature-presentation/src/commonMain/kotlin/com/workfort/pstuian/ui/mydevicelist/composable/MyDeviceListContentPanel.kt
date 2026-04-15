package com.workfort.pstuian.ui.mydevicelist.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.common.composable.AnimatedEmptyView
import com.workfort.pstuian.common.composable.AnimatedErrorView
import com.workfort.pstuian.common.composable.TitleTextSmall
import com.workfort.pstuian.common.composable.isLastItemVisible
import com.workfort.pstuian.featuredomain.model.DeviceEntity
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListUiEvent
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListUiState

@Composable
internal fun MyDeviceListContentPanel(
    uiState: MyDeviceListUiState,
    onUiEvent: (MyDeviceListUiEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (uiState.devices.isEmpty()) {
            if (uiState.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AnimatedErrorView(modifier = Modifier.width(200.dp))
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AnimatedEmptyView(modifier = Modifier.width(200.dp))
                }
            }
        } else {
            DeviceListView(
                devices = uiState.devices,
                isLoading = uiState.isLoading,
                onUiEvent = onUiEvent,
            )
        }
    }
}

@Composable
private fun DeviceListView(
    devices: List<DeviceEntity>,
    isLoading: Boolean,
    onUiEvent: (MyDeviceListUiEvent) -> Unit,
) {
    val listState = rememberLazyListState()
    val isLastItemVisible by remember {
        derivedStateOf {
            listState.isLastItemVisible
        }
    }

    LaunchedEffect(key1 = isLastItemVisible) {
        if (isLastItemVisible) {
            onUiEvent(MyDeviceListUiEvent.LoadMore)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(devices) { item ->
            DeviceItemView(
                item = item,
                onClick = {
                    onUiEvent(MyDeviceListUiEvent.ItemClicked(item))
                },
            )
        }
        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun DeviceItemView(
    item: DeviceEntity,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = "Edit Icon",
            tint = Color.Gray,
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            TitleTextSmall(text = item.model ?: "Unknown")
            Text(text = "IP Address: ${item.ipAddress}")
            Text(text = "Last Activity: ${item.updatedAt}")
        }
    }
}
