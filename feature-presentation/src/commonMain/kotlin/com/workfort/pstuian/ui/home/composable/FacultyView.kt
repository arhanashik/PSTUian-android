package com.workfort.pstuian.ui.home.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.SingletonImageLoader
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.workfort.pstuian.featuredomain.model.Faculty
import com.workfort.pstuian.ui.common.composable.LoadAsyncImage
import com.workfort.pstuian.ui.common.composable.extractBackdropTintFromImage
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import com.workfort.pstuian.ui.common.theme.bgCircle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.painterResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.ic_education_gray

@Composable
fun FacultyView(
    modifier: Modifier,
    faculty: Faculty,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(24.dp)
    val defaultSurface = MaterialTheme.colorScheme.surface
    val themeBackground = MaterialTheme.colorScheme.background
    val isDarkTheme = themeBackground.luminance() < 0.5f
    val useIconTintBackground = !isDarkTheme
    val placeholderPainter = painterResource(Res.drawable.ic_education_gray)

    if (faculty.icon.isNullOrEmpty()) {
        Card(
            modifier = modifier
                .clip(shape)
                .clickable(onClick = onClick),
            colors = CardDefaults.cardColors(containerColor = defaultSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = shape,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .bgCircle()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    LoadAsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        url = faculty.icon,
                        placeholder = Res.drawable.ic_education_gray,
                        contentScale = ContentScale.Fit,
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = faculty.shortTitle,
                    style = TextStyle.label1.copy(
                        color = AppColors.textSecondary,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
        }
        return
    }

    val platformContext = LocalPlatformContext.current
    val imageLoader = remember(platformContext) { SingletonImageLoader.get(platformContext) }
    val request = remember(faculty.icon, platformContext) {
        ImageRequest.Builder(platformContext)
            .data(faculty.icon)
            .crossfade(true)
            .build()
    }
    var containerColor by remember(faculty.icon, defaultSurface) { mutableStateOf(defaultSurface) }
    LaunchedEffect(isDarkTheme, defaultSurface) {
        if (isDarkTheme) {
            containerColor = defaultSurface
        }
    }

    val surfaceLatest = rememberUpdatedState(defaultSurface)
    val useTint = rememberUpdatedState(useIconTintBackground)
    val painter = rememberAsyncImagePainter(
        model = request,
        imageLoader = imageLoader,
        placeholder = placeholderPainter,
        error = placeholderPainter,
        contentScale = ContentScale.Fit,
    )

    LaunchedEffect(painter, faculty.icon, defaultSurface, useIconTintBackground) {
        // Collect Coil's StateFlow directly — snapshotFlow(...) misses successes because Coil does not
        // write through Compose snapshots, so fast memory-cache loads never trigger a snapshotFlow emit.
        painter.state.collectLatest { state ->
            when (state) {
                is AsyncImagePainter.State.Success -> {
                    val surface = surfaceLatest.value
                    if (!useTint.value) {
                        containerColor = surface
                    } else {
                        val tint = withContext(Dispatchers.Default) {
                            extractBackdropTintFromImage(state.result.image)
                        }
                        containerColor = tint ?: surface
                    }
                }
                is AsyncImagePainter.State.Error -> {
                    containerColor = surfaceLatest.value
                }
                else -> Unit
            }
        }
    }

    Card(
        modifier = modifier
            .clip(shape)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = shape,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .bgCircle()
                    .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                )
            }
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = faculty.shortTitle,
                style = TextStyle.label1.copy(
                    color = AppColors.textSecondary,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
    }
}
