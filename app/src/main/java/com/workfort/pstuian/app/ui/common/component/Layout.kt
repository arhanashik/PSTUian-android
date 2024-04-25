package com.workfort.pstuian.app.ui.common.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun CardWithAnimatedBorder(
    modifier: Modifier = Modifier,
    borderSize: Dp = 1.dp,
    borderColors: List<Color> = listOf(Color.Gray, Color.White),
    shape: Shape = RoundedCornerShape(8.dp),
    durationMills: Int = 2500,
    onCardClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec =
        infiniteRepeatable(
            animation = tween(durationMills, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "",
    )
    val brush = Brush.sweepGradient(borderColors)

    Surface(
        modifier = modifier.clickable { onCardClick() },
        shape = shape,
    ) {
        Surface(
            modifier = Modifier
                .clipToBounds()
                .padding(borderSize)
                .drawWithContent {
                    rotate(angle) {
                        drawCircle(
                            brush = brush,
                            radius = size.width,
                            blendMode = BlendMode.SrcIn,
                        )
                    }
                drawContent()
            },
            shape = shape,
        ) {
            content()
        }
    }
}