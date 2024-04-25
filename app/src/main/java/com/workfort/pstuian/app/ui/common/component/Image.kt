package com.workfort.pstuian.app.ui.common.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.workfort.pstuian.R

@Composable
fun LoadAsyncUserImage(
    modifier: Modifier = Modifier,
    url: String?,
    size: Dp,
    @DrawableRes placeholder: Int = R.drawable.img_placeholder_profile,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = CircleShape,
) {
    LoadAsyncImage(
        modifier = modifier
            .clip(shape)
            .size(size),
        url = url,
        placeholder = placeholder,
        contentScale = contentScale,
    )
}

@Composable
fun LoadAsyncImage(
    modifier: Modifier = Modifier,
    url: String?,
    @DrawableRes placeholder: Int,
    contentScale: ContentScale,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        placeholder = painterResource(placeholder),
        error = painterResource(placeholder),
        contentDescription = "Async Image",
        contentScale = contentScale,
        modifier = modifier,
    )
}