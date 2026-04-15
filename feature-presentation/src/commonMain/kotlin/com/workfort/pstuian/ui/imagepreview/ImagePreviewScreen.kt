package com.workfort.pstuian.ui.imagepreview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.workfort.pstuian.common.composable.AnimatedErrorView
import com.workfort.pstuian.common.composable.AnimatedImagePlaceholderView

@Composable
fun ImagePreviewScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    imageUrl: String,
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
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 4.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Image Preview",
                )
            }
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu icon",
                )
            }
        }

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(imageUrl)
                .size(Size.ORIGINAL)
                .build()
        )

        when (painter.state) {
            is AsyncImagePainter.State.Success -> {
                Image(
                    modifier = modifier.fillMaxWidth(),
                    painter = painter,
                    contentDescription = "Image Preview",
                    contentScale = ContentScale.FillWidth,
                )
            }
            is AsyncImagePainter.State.Empty,
            is AsyncImagePainter.State.Loading -> {
                AnimatedImagePlaceholderView()
            }
            is AsyncImagePainter.State.Error -> {
                AnimatedErrorView()
            }
            else -> {}
        }
    }
}