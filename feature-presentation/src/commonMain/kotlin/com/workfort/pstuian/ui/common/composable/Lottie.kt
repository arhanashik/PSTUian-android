package com.workfort.pstuian.ui.common.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.alexzhirkevich.compottie.LottieAnimation
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.LottieConstants
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import pstuian.feature_presentation.generated.resources.Res

@Composable
fun AnimatedListLoaderView(modifier: Modifier = Modifier) {
    LottieAnimationView(
        modifier = modifier,
        resourcePath = "files/list_loading.json",
    )
}

@Composable
fun AnimatedEmptyView(modifier: Modifier = Modifier) {
    LottieAnimationView(
        modifier = modifier,
        resourcePath = "files/empty_box.json",
    )
}

@Composable
fun AnimatedImagePlaceholderView(modifier: Modifier = Modifier) {
    LottieAnimationView(
        modifier = modifier,
        resourcePath = "files/image_placeholder.json",
    )
}

@Composable
fun AnimatedErrorView(modifier: Modifier = Modifier) {
    LottieAnimationView(
        modifier = modifier,
        resourcePath = "files/error_cat.json",
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LottieAnimationView(
    modifier: Modifier = Modifier,
    resourcePath: String,
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes(resourcePath).decodeToString()
        )
    }

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier,
    )
}
