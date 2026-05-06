package com.workfort.pstuian.ui.donation.donors.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.Donor
import com.workfort.pstuian.ui.common.composable.AnimatedEmptyView
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.common.theme.bgCircle
import com.workfort.pstuian.ui.donation.donors.state.DonorsUiEvent
import com.workfort.pstuian.ui.donation.donors.state.DonorsUiState

@Composable
fun DonorsContentPanel(
    uiState: DonorsUiState.Content,
    onUiEvent: (DonorsUiEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (uiState.donorList.isEmpty()) {
            EmptyView()
        } else {
            DonorListView(uiState.donorList, uiState.showLoadingMore, onUiEvent)
        }
    }
}

@Composable
private fun LoadingView() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyView() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedEmptyView()
    }
}

@Composable
private fun DonorListView(
    donorList: List<Donor>,
    showLoadingMore: Boolean,
    onUiEvent: (DonorsUiEvent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
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
        if (showLoadingMore) {
            item {
                LoadingView()
            }
        }
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
            .clickable { onClickItem(item) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .bgCircle()
                    .padding(8.dp),
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                TitleTextSmall(text = item.name ?: "Donation Info")
                item.info?.let { Text(text = it) }
                Text(text = "Reference: ${item.reference}")
            }
        }
    }
}
