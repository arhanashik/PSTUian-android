package com.workfort.pstuian.ui.common.composable

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.theme.TextStyle

@Composable
internal fun ToggleSwitch(
    options: List<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 40.dp,
    cornerRadius: Dp = 20.dp,
) {
    if (options.isEmpty()) return

    val normalizedIndex = selectedIndex.coerceIn(0, options.lastIndex)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
    ) {
        val segmentWidth = maxWidth / options.size
        val indicatorOffset by animateDpAsState(
            targetValue = segmentWidth * normalizedIndex + 2.dp,
            animationSpec = spring(
                dampingRatio = 0.9f,
                stiffness = 420f,
            ),
            label = "segmentedToggleIndicatorOffset",
        )
        val segmentHeight = (height - 4.dp).coerceAtLeast(0.dp)
        val interactionSource = remember { MutableInteractionSource() }

        Box(
            modifier = Modifier
                .offset(x = indicatorOffset, y = 2.dp)
                .height(segmentHeight)
                .fillMaxWidth(1f / options.size)
                .clip(RoundedCornerShape((cornerRadius - 2.dp).coerceAtLeast(0.dp)))
                .background(MaterialTheme.colorScheme.primary),
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, label ->
                val segmentShape = when (index) {
                    0 -> RoundedCornerShape(topStart = cornerRadius, bottomStart = cornerRadius)
                    options.lastIndex -> RoundedCornerShape(topEnd = cornerRadius, bottomEnd = cornerRadius)
                    else -> RoundedCornerShape(0.dp)
                }
                val isSelected = index == normalizedIndex

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(segmentShape)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                        ) {
                            if (!isSelected) {
                                onSelectedIndexChange(index)
                            }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        style = TextStyle.label1,
                    )
                }
            }
        }
    }
}
