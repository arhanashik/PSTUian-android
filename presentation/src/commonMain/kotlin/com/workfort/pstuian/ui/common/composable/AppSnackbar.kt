package com.workfort.pstuian.ui.common.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import kotlinx.coroutines.delay

@Composable
fun AppSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val currentSnackbarData = hostState.currentSnackbarData

    // Handle the snackbar duration and dismissal
    LaunchedEffect(currentSnackbarData) {
        if (currentSnackbarData != null) {
            val duration = when (currentSnackbarData.visuals.duration) {
                SnackbarDuration.Short -> 4000L
                SnackbarDuration.Long -> 10000L
                SnackbarDuration.Indefinite -> Long.MAX_VALUE
            }
            delay(duration)
            currentSnackbarData.dismiss()
        }
    }

    AnimatedContent(
        targetState = currentSnackbarData,
        modifier = modifier,
        transitionSpec = {
            (slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 400)
            ) + fadeIn(animationSpec = tween(durationMillis = 400))).togetherWith(
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 400)
                ) + fadeOut(animationSpec = tween(durationMillis = 400))
            )
        },
        contentAlignment = Alignment.BottomCenter,
        label = "SnackbarAnimation",
    ) { data ->
        if (data != null) {
            AppSnackbar(data)
        }
    }
}

@Composable
fun AppSnackbar(data: SnackbarData) {
    Snackbar(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
        containerColor = AppColors.card,
        contentColor = AppColors.textPrimary,
        actionContentColor = AppColors.primary,
    ) {
        Text(
            text = data.visuals.message,
            style = TextStyle.body2,
        )
    }
}

@Composable
fun HandleSnackbar(
    message: String,
    snackbarHostState: SnackbarHostState,
    onSnackbarShown: () -> Unit,
) {
    LaunchedEffect(message) {
        snackbarHostState.showSnackbar(message)
        onSnackbarShown()
    }
}
