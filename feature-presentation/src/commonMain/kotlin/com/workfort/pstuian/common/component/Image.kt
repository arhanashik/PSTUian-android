package com.workfort.pstuian.common.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.img_placeholder_profile

@Composable
fun LoadAsyncUserImage(
    modifier: Modifier = Modifier,
    url: String?,
    size: Dp,
    placeholder: DrawableResource = Res.drawable.img_placeholder_profile,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = CircleShape,
) {
    LoadAsyncImage(
        modifier = modifier
            .clip(shape)
            .size(size),
        url = url,
        placeholder = painterResource(placeholder),
        contentScale = contentScale,
    )
}

@Composable
fun LoadAsyncImage(
    modifier: Modifier = Modifier,
    url: String?,
    placeholder: DrawableResource,
    contentScale: ContentScale,
) {
    LoadAsyncImage(
        modifier = modifier,
        url = url,
        placeholder = painterResource(placeholder),
        contentScale = contentScale,
    )
}

@Composable
fun LoadAsyncImage(
    modifier: Modifier = Modifier,
    url: String?,
    placeholder: ImageVector,
    contentScale: ContentScale,
) {
    LoadAsyncImage(
        modifier = modifier,
        url = url,
        placeholder = rememberVectorPainter(placeholder),
        contentScale = contentScale,
    )
}

@Composable
private fun LoadAsyncImage(
    modifier: Modifier = Modifier,
    url: String?,
    placeholder: androidx.compose.ui.graphics.painter.Painter,
    contentScale: ContentScale,
) {
    AsyncImage(
        model = ImageRequest.Builder(coil3.compose.LocalPlatformContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        placeholder = placeholder,
        error = placeholder,
        contentDescription = "Async Image",
        contentScale = contentScale,
        modifier = modifier,
    )
}
