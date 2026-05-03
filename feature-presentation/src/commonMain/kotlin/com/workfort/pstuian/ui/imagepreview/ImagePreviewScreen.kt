package com.workfort.pstuian.ui.imagepreview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Size
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.AnimatedImagePlaceholderView
import com.workfort.pstuian.ui.common.composable.TopBarCircleButton
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_go_back

@Composable
fun ImagePreviewScreen(
    imageUrl: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold { innerPadding ->
        ImagePreviewScreenComponent(
            modifier = modifier.padding(innerPadding),
            onBack = onBack,
            imageUrl = imageUrl,
        )
    }
}

@Composable
private fun ImagePreviewScreenComponent(
    modifier: Modifier,
    onBack: () -> Unit,
    imageUrl: String,
) {
    val request = ImageRequest.Builder(LocalPlatformContext.current)
        .data(imageUrl)
        .size(Size.ORIGINAL)
        .crossfade(true)
        .build()

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        SubcomposeAsyncImage(
            model = request,
            contentDescription = "Image Preview",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    AnimatedImagePlaceholderView()
                }
            },
            error = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    AnimatedErrorView()
                }
            },
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
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
