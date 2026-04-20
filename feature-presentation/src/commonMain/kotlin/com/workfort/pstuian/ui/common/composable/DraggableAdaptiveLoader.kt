package com.workfort.pstuian.ui.common.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun DraggableAdaptiveLoader(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    zIndex: Float = 1f,
    showAsOverLay: Boolean = false,
    showFilledLoader: Boolean = true,
) {
    val coroutineScope = rememberCoroutineScope()
    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }

    Box(modifier = Modifier.fillMaxSize().zIndex(zIndex)) {
        if (showAsOverLay) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .pointerInput(Unit) {}, // Consume touches
            )
        }

        Box(
            modifier = modifier.align(Alignment.Center)
                .offset { IntOffset(offset.value.x.roundToInt(), offset.value.y.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            coroutineScope.launch {
                                offset.snapTo(offset.value + dragAmount)
                            }
                        },
                        onDragEnd = {
                            coroutineScope.launch {
                                offset.animateTo(
                                    targetValue = Offset(0f, 0f),
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center,
        ) {
            if (showFilledLoader) {
                AdaptiveCircularLoader(modifier = Modifier.size(size))
            } else {
                SpiralCircularLoader(modifier = Modifier.size(size))
            }
        }
    }
}

@Composable
fun AdaptiveCircularLoader(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing)
        )
    )

    val colors = listOf(
        Color(0xFF5AC8FA), // Light Blue
        Color(0xFF5856D6), // Blue
        Color(0xFFAF52DE), // Purple
        Color(0xFFFF2D55), // Pink
        Color(0xFFFF3B30), // Red
        Color(0xFFFF9500), // Orange
        Color(0xFFFFCC00), // Yellow
        Color(0xFF4CD964), // Green
        Color(0xFF5AC8FA), // Back to Light Blue
    )

    Canvas(modifier = modifier.graphicsLayer { rotationZ = rotation }) {
        val layers = 40
        val maxRadius = size.minDimension / 2
        // Draw many overlapping circles with progressive rotation to create "bent" colors
        for (i in 0 until layers) {
            val progress = i.toFloat() / layers
            val radius = maxRadius * (1f - progress)
            // Twist effect: inner layers are rotated more than outer layers
            val twist = progress * 60f
            rotate(twist) {
                drawCircle(
                    brush = Brush.sweepGradient(colors),
                    radius = radius,
                    center = center
                )
            }
        }
    }
}

@Composable
fun SpiralCircularLoader(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing)
        )
    )

    val colors = listOf(
        Color(0xFF5AC8FA), // Light Blue
        Color(0xFF5856D6), // Blue
        Color(0xFFAF52DE), // Purple
        Color(0xFFFF2D55), // Pink
        Color(0xFFFF3B30), // Red
        Color(0xFFFF9500), // Orange
        Color(0xFFFFCC00), // Yellow
        Color(0xFF4CD964), // Green
        Color(0xFF5AC8FA), // Back to Light Blue
    )

    Canvas(modifier = modifier.graphicsLayer { rotationZ = rotation }) {
        val strokeWidth = size.minDimension * 0.15f
        val arcSize = size.minDimension - strokeWidth
        
        // Create a "bent" spiral effect by drawing multiple layers with progressive rotation
        val layers = 24
        for (i in 0 until layers) {
            val progress = i.toFloat() / layers
            // Twist effect: inner layers are rotated more than outer layers
            val twist = progress * 60f 
            val layerSize = arcSize - (progress * strokeWidth * 0.6f)
            
            rotate(twist) {
                drawArc(
                    brush = Brush.sweepGradient(colors),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(
                        x = (size.width - layerSize) / 2,
                        y = (size.height - layerSize) / 2
                    ),
                    size = Size(layerSize, layerSize),
                    style = Stroke(width = strokeWidth / layers * 2.5f, cap = StrokeCap.Round)
                )
            }
        }
    }
}
