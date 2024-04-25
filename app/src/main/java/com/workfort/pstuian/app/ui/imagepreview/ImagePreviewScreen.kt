package com.workfort.pstuian.app.ui.imagepreview

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.workfort.pstuian.app.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.app.ui.common.component.AnimatedImagePlaceholderView
import com.workfort.pstuian.app.ui.common.theme.LottieAnimation

@Composable
fun ImagePreviewScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    imageUrl: String,
) {
    Scaffold { innerPadding ->
        ImagePreviewScreenComponent(
            modifier = modifier.padding(innerPadding),
            navController = navController,
            imageUrl = imageUrl,
        )
    }
}

@Composable
private fun ImagePreviewScreenComponent(
    modifier: Modifier,
    navController: NavHostController,
    imageUrl: String,
) {
    val context = LocalContext.current
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
            IconButton(onClick = { navController.popBackStack() }) {
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
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .size(Size.ORIGINAL) // Set the target size to load the image at.
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
                AnimatedImagePlaceholderView(modifier = Modifier.width(LottieAnimation.errorWidth))
            }
            is AsyncImagePainter.State.Error -> {
                AnimatedErrorView(modifier = Modifier.width(LottieAnimation.errorWidth))
            }
        }
    }
}