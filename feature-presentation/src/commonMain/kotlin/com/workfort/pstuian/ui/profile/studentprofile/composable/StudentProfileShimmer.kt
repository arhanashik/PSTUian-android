package com.workfort.pstuian.ui.profile.studentprofile.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.shimmerAnimation

@Composable
internal fun StudentProfileShimmer() {
    val headerCardGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
            MaterialTheme.colorScheme.surface,
        ),
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar skeleton (outside header card)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ShimmerCircle(size = 44.dp)
            ShimmerRect(width = 120.dp, height = 16.dp, radius = 6.dp)
            ShimmerCircle(size = 44.dp)
        }

        // Header card (no elevation, slight gradient)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(headerCardGradient),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                // Avatar + name row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ShimmerCircle(size = 72.dp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        ShimmerRect(width = 160.dp, height = 22.dp, radius = 6.dp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ShimmerRect(width = 80.dp, height = 34.dp, radius = 17.dp)
                            ShimmerRect(width = 90.dp, height = 34.dp, radius = 17.dp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Faculty row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ShimmerCircle(size = 16.dp)
                    ShimmerRect(width = 200.dp, height = 14.dp, radius = 4.dp)
                }

                Spacer(modifier = Modifier.height(7.dp))

                // Batch row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ShimmerCircle(size = 16.dp)
                    ShimmerRect(width = 150.dp, height = 14.dp, radius = 4.dp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bio lines
                ShimmerRect(radius = 4.dp, height = 12.dp, modifier = Modifier.fillMaxWidth(0.9f))
                Spacer(modifier = Modifier.height(5.dp))
                ShimmerRect(radius = 4.dp, height = 12.dp, modifier = Modifier.fillMaxWidth(0.7f))
            }
        }

        // Toggle shimmer (outside content card)
        ShimmerRect(
            radius = 20.dp,
            height = 42.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        )

        // Content card skeleton (under toggle)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface),
        ) {
            // List item skeletons
            repeat(6) { index ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                ) {
                    ShimmerRect(
                        radius = 4.dp,
                        height = 11.dp,
                        modifier = Modifier.fillMaxWidth(if (index % 2 == 0) 0.3f else 0.4f),
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    ShimmerRect(
                        radius = 4.dp,
                        height = 16.dp,
                        modifier = Modifier.fillMaxWidth(if (index % 3 == 0) 0.55f else 0.7f),
                    )
                }
            }
        }
    }
}

// ── Shimmer primitives ────────────────────────────────────────────────────────

@Composable
private fun ShimmerCircle(size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .shimmerAnimation(),
    )
}

@Composable
private fun ShimmerRect(
    height: Dp,
    radius: Dp,
    modifier: Modifier = Modifier,
    width: Dp? = null,
) {
    val sizeModifier = if (width != null) modifier.width(width) else modifier
    Box(
        modifier = sizeModifier
            .height(height)
            .clip(RoundedCornerShape(radius))
            .shimmerAnimation(),
    )
}
