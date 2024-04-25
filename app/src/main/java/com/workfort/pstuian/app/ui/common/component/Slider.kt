package com.workfort.pstuian.app.ui.common.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.workfort.pstuian.R
import com.workfort.pstuian.model.SliderEntity
import kotlinx.coroutines.delay
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

    LaunchedEffect(key1 = pagerState.currentPage) {
        onScrollSlider(pagerState.currentPage)
    }

    // Auto play
    LaunchedEffect(key1 = autoPlay) {
        while (autoPlay) {
            delay(2500)
            with(pagerState) {
                animateScrollToPage(page = (currentPage + 1) % sliders.size)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 24.dp),
        ) { page ->
            val pageOffset = pagerState.currentPage - page + pagerState.currentPageOffsetFraction
            Card(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .graphicsLayer {
                        cameraDistance = 8 * density
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
                        )
                        rotationY = lerp(
                            start = 0f,
                            stop = 0f,
                            fraction = pageOffset.coerceIn(-1f, 1f),
                        )
                        scaleY = lerp(
                            start = 0.8f,
                            stop = 1f,
                            fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                        )
                    }
                    .clickable {
                        onClickSlider(sliders[page])
                    },
                shape = RoundedCornerShape(12.dp),
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LoadAsyncImage(
                        url = sliders[page].imageUrl,
                        placeholder = R.drawable.img_placeholder_profile,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(12.dp)),
                    )
                    sliders[page].title?.let {
                        Text(
                            text = it,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp), horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) {
                    Color.DarkGray
                } else {
                    Color.LightGray
                }
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(6.dp)
                )
            }
        }
    }
}