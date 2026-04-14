package com.workfort.pstuian.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.workfort.pstuian.common.theme.AppColors

@Composable
fun LoadingOverlay(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black.copy(alpha = 0.4f),
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .pointerInput(Unit) {},
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = AppColors.primary)
    }
}
