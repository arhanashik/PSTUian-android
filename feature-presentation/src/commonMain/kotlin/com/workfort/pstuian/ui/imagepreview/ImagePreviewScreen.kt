package com.workfort.pstuian.ui.imagepreview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.SingletonImageLoader
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Size
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.AnimatedImagePlaceholderView
import com.workfort.pstuian.ui.common.composable.TopBarCircleButton
import com.workfort.pstuian.ui.common.composable.extractBackdropTintFromImage
import com.workfort.pstuian.ui.common.composable.IMAGE_PREVIEW_CHROMA_AMPLIFY
import com.workfort.pstuian.ui.common.composable.IMAGE_PREVIEW_NEUTRAL_BLEND
import com.workfort.pstuian.ui.common.theme.ApplySystemBarColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_go_back

@Composable
fun ImagePreviewScreen(
    imageUrl: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val defaultBg = MaterialTheme.colorScheme.background
    val isDarkTheme = defaultBg.luminance() < 0.5f
    val useImageTintBackground = !isDarkTheme

    var scaffoldBackground by remember(imageUrl, defaultBg) { mutableStateOf(defaultBg) }

    LaunchedEffect(isDarkTheme, defaultBg) {
        if (isDarkTheme) {
            scaffoldBackground = defaultBg
        }
    }

    val darkIconsOnBars = scaffoldBackground.luminance() > 0.5f
    ApplySystemBarColors(
        statusBarColor = scaffoldBackground,
        statusBarDarkIcons = darkIconsOnBars,
        navigationBarColor = scaffoldBackground,
        navigationBarDarkIcons = darkIconsOnBars,
    )

    Scaffold(
        containerColor = scaffoldBackground,
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        ImagePreviewScreenComponent(
            modifier = modifier.padding(innerPadding),
            onBack = onBack,
            imageUrl = imageUrl,
            defaultBackground = defaultBg,
            useImageTintBackground = useImageTintBackground,
            onScaffoldBackgroundChange = { scaffoldBackground = it },
        )
    }
}

@Composable
private fun ImagePreviewScreenComponent(
    modifier: Modifier,
    onBack: () -> Unit,
    imageUrl: String,
    defaultBackground: Color,
    useImageTintBackground: Boolean,
    onScaffoldBackgroundChange: (Color) -> Unit,
) {
    val platformContext = LocalPlatformContext.current
    val imageLoader = remember(platformContext) { SingletonImageLoader.get(platformContext) }
    val request = remember(imageUrl, platformContext) {
        ImageRequest.Builder(platformContext)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .crossfade(true)
            .build()
    }
    val painter = rememberAsyncImagePainter(
        model = request,
        imageLoader = imageLoader,
        contentScale = ContentScale.Fit,
    )
    val imageLoadState by painter.state.collectAsState()

    val onBg = rememberUpdatedState(onScaffoldBackgroundChange)
    val fallbackBg = rememberUpdatedState(defaultBackground)
    val useImageTint = rememberUpdatedState(useImageTintBackground)

    LaunchedEffect(painter, imageUrl, defaultBackground, useImageTintBackground) {
        painter.state.collectLatest { state ->
            when (state) {
                is AsyncImagePainter.State.Success -> {
                    if (!useImageTint.value) {
                        onBg.value(fallbackBg.value)
                    } else {
                        val tint = withContext(Dispatchers.Default) {
                            extractBackdropTintFromImage(
                                state.result.image,
                                neutralBlend = IMAGE_PREVIEW_NEUTRAL_BLEND,
                                chromaAmplify = IMAGE_PREVIEW_CHROMA_AMPLIFY,
                            )
                        }
                        onBg.value(tint ?: fallbackBg.value)
                    }
                }
                is AsyncImagePainter.State.Error -> onBg.value(fallbackBg.value)
                else -> Unit
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (imageLoadState) {
                is AsyncImagePainter.State.Success -> {
                    Image(
                        painter = painter,
                        contentDescription = "Image Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit,
                    )
                }
                is AsyncImagePainter.State.Error -> {
                    AnimatedErrorView()
                }
                else -> {
                    AnimatedImagePlaceholderView()
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TopBarCircleButton(
                icon = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = stringResource(Res.string.txt_go_back),
                onClick = onBack,
            )
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.size(48.dp))
        }
    }
}
