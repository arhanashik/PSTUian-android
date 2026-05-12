package com.workfort.pstuian.ui.common.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import pstuian.presentation.generated.resources.Res

@Composable
fun AnimatedListLoaderView(modifier: Modifier = Modifier) {
    LottieAnimationView(
        modifier = modifier,
        resourcePath = "files/list_loading.json",
    )
}

@Composable
fun AnimatedEmptyView(modifier: Modifier = Modifier.size(200.dp)) {
    LottieAnimationView(
        modifier = modifier,
        resourcePath = "files/empty_box.json",
    )
}

@Composable
fun AnimatedImagePlaceholderView(modifier: Modifier = Modifier.size(200.dp)) {
    LottieAnimationView(
        modifier = modifier,
        resourcePath = "files/image_placeholder.json",
    )
}

@Composable
fun AnimatedErrorView(modifier: Modifier = Modifier.size(250.dp)) {
    LottieAnimationView(
        modifier = modifier,
        resourcePath = "files/something_went_wrong.json",
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LottieAnimationView(
    modifier: Modifier = Modifier,
    resourcePath: String,
    contentDescription: String = "Lottie animation",
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes(resourcePath).decodeToString()
        )
    }

    val progress by animateLottieCompositionAsState(composition)

    Image(
        modifier = modifier,
        painter = rememberLottiePainter(
            composition = composition,
            progress = { progress },
        ),
        contentDescription = contentDescription,
    )
}
