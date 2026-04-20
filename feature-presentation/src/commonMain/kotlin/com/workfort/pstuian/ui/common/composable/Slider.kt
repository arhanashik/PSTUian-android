package com.workfort.pstuian.ui.common.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOutQuart
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.workfort.pstuian.featuredomain.model.SliderEntity
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import kotlinx.coroutines.delay
import pstuian.feature_presentation.generated.resources.Res
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import kotlin.math.absoluteValue


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SliderView(
    sliders: List<SliderEntity>,
    scrollPosition: Int,
    autoPlay: Boolean = true,
    onScrollSlider: (Int) -> Unit,
    onClickSlider: (SliderEntity) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = scrollPosition,
        pageCount = { sliders.size },
    )
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    var isForward by remember { mutableStateOf(true) }
    var lastPage by remember { mutableIntStateOf(pagerState.currentPage) }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { currentPage ->
            onScrollSlider(currentPage)
            
            // Update direction based on navigation (manual or auto)
            if (currentPage > lastPage) {
                isForward = true
            } else if (currentPage < lastPage) {
                isForward = false
            }
            
            // Boundary detection still takes precedence for auto-play reversal
            if (pagerState.pageCount > 1) {
                if (currentPage == pagerState.pageCount - 1) {
                    isForward = false
                } else if (currentPage == 0) {
                    isForward = true
                }
            }
            lastPage = currentPage
        }
    }

    // Auto play with reversal logic and user interaction handling
    LaunchedEffect(key1 = autoPlay, key2 = isDragged) {
        if (!autoPlay || isDragged) return@LaunchedEffect

        while (true) {
            delay(5000)
            if (pagerState.pageCount > 1) {
                val targetPage = if (isForward) {
                    pagerState.currentPage + 1
                } else {
                    pagerState.currentPage - 1
                }

                pagerState.animateScrollToPage(
                    page = targetPage,
                    animationSpec = tween(
                        durationMillis = 1500,
                        easing = EaseInOutQuart
                    )
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 48.dp),
            pageSpacing = 24.dp,
        ) { page ->
            val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
            val absOffset = pageOffset.absoluteValue

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        // Scale effect for the card
                        val scaleXEffect = lerp(
                            start = 0.9f,
                            stop = 1f,
                            fraction = 1f - absOffset.coerceIn(0f, 1f)
                        )
                        val scaleYEffect = lerp(
                            start = 0.8f, // Slightly less height for side slides
                            stop = 1f,
                            fraction = 1f - absOffset.coerceIn(0f, 1f)
                        )
                        scaleX = scaleXEffect
                        scaleY = scaleYEffect

                        // Alpha effect
                        alpha = lerp(
                            start = 0.7f,
                            stop = 1f,
                            fraction = 1f - absOffset.coerceIn(0f, 1f)
                        )

                        // Rotation effect for "bended" look
                        // We shift the pivot to the inner edge (closer to center) to bring it "front"
                        val pivotX = if (pageOffset > 0) 1f else if (pageOffset < 0) 0f else 0.5f
                        transformOrigin = TransformOrigin(pivotX, 0.5f)
                        rotationY = pageOffset * -45f

                        // Camera distance for 3D perspective - smaller value makes it more dramatic
                        cameraDistance = 8f * density

                        // Slightly pull side cards towards the center to emphasize the bend
                        translationX = pageOffset * 20f
                    }
                    .clickable {
                        onClickSlider(sliders[page])
                    },
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Zoom effect for the image inside the frame
                    val imageScale = lerp(
                        start = 1f,
                        stop = 1.3f,
                        fraction = 1f - absOffset.coerceIn(0f, 1f)
                    )

                    // Better placeholder: centered icon with gray background
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.LightGray.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            tint = Color.Gray.copy(alpha = 0.3f),
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    AsyncImage(
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(sliders[page].imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                            .graphicsLayer {
                                scaleX = imageScale
                                scaleY = imageScale
                                // Parallax effect
                                translationX = pageOffset * 40f
                            },
                    )

                    // Scrims for text readability
                    // Top scrim for title
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .align(Alignment.TopCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.5f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .graphicsLayer {
                                alpha = 1f - absOffset.coerceIn(0f, 1f)
                            }
                    )

                    // Bottom scrim for overall aesthetics
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.3f)
                                    )
                                )
                            )
                    )

                    sliders[page].title?.let {
                        Text(
                            text = it,
                            color = Color.White,
                            style = TextStyle.label1.copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(16.dp)
                                .graphicsLayer {
                                    // Fade and slide animation for title
                                    alpha = 1f - (absOffset * 2f).coerceIn(0f, 1f)
                                    translationY = -absOffset * 40f
                                }
                        )
                    }
                }
            }
        }
        Row(
            Modifier
                .height(32.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val pageOffset = (pagerState.currentPage - iteration) + pagerState.currentPageOffsetFraction
                val absOffset = pageOffset.absoluteValue
                val fraction = (1f - absOffset).coerceIn(0f, 1f)

                val dotWidth = androidx.compose.ui.unit.lerp(6.dp, 16.dp, fraction)
                val dotColor = androidx.compose.ui.graphics.lerp(Color.LightGray.copy(alpha = 0.5f), AppColors.primary, fraction)

                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                        .size(width = dotWidth, height = 6.dp)
                )
            }
        }
    }
}
