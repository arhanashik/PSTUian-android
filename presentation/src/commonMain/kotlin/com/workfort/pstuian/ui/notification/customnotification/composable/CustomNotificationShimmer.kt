package com.workfort.pstuian.ui.notification.customnotification.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.shimmerAnimation
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.notification.common.composable.notificationCardElevation

@Composable
internal fun CustomNotificationShimmer() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.card),
        elevation = notificationCardElevation(isUnread = true),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .shimmerAnimation(),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.42f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerAnimation(),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(18.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerAnimation(),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .width(72.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerAnimation(),
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.72f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerAnimation(),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.48f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerAnimation(),
                )
            }
        }
    }
}
