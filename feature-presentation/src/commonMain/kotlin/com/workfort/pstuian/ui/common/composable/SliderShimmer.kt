package com.workfort.pstuian.ui.common.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun SliderShimmer() {
    val peekWidth = 48.dp // 64dp padding - 16dp spacing
    val centerPadding = 64.dp

    Box(modifier = Modifier.fillMaxSize()) {
        // Left Peek
        Box(
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .width(peekWidth)
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp))
                .shimmerAnimation()
        )

        // Center Card
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = centerPadding)
                .clip(RoundedCornerShape(24.dp))
                .shimmerAnimation()
        )

        // Right Peek
        Box(
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .width(peekWidth)
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp))
                .shimmerAnimation()
        )
    }
}
