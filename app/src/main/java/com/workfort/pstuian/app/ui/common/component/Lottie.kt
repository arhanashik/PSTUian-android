package com.workfort.pstuian.app.ui.common.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.workfort.pstuian.R


@Composable
fun AnimatedListLoaderView(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.list_loading)
    )
    LottieAnimationView(modifier = modifier, composition = composition)
}

@Composable
fun AnimatedEmptyView(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.empty_box)
    )
    LottieAnimationView(modifier = modifier, composition = composition)
}

@Composable
fun AnimatedImagePlaceholderView(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.image_placeholder)
    )
    LottieAnimationView(modifier = modifier, composition = composition)
}

@Composable
fun AnimatedErrorView(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.error_cat)
    )
    LottieAnimationView(modifier = modifier, composition = composition)
}

@Composable
fun LottieAnimationView(
    modifier: Modifier = Modifier,
    composition: LottieComposition?,
) {
    val progressState by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
    )

    LottieAnimation(
        composition = composition,
        progress = progressState,
        modifier = modifier,
    )
}