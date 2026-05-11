package com.workfort.pstuian.ui.common.composable

import android.graphics.Bitmap
import android.os.Build
import androidx.compose.ui.graphics.Color
import coil3.BitmapImage
import coil3.Image
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private fun ensureArgb8888(bitmap: Bitmap): Bitmap {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && bitmap.config == Bitmap.Config.HARDWARE) {
        bitmap.copy(Bitmap.Config.ARGB_8888, false) ?: bitmap
    } else {
        bitmap
    }
}

internal actual suspend fun extractBackdropTintFromImage(
    image: Image,
    neutralBlend: Float,
    chromaAmplify: Float,
): Color? =
    withContext(Dispatchers.Default) {
        runCatching {
            val sampled: Bitmap =
                when (image) {
                    is BitmapImage -> {
                        val src = ensureArgb8888(image.bitmap)
                        val (tw, th) = backdropSampleDimensions(src.width, src.height)
                        Bitmap.createScaledBitmap(src, tw, th, true)
                    }
                    else -> {
                        val (tw, th) = backdropSampleDimensions(image)
                        if (tw <= 0 || th <= 0) return@withContext null
                        ensureArgb8888(image.toBitmap(tw, th))
                    }
                }
            val avg = averageInterestingRgb(sampled.width, sampled.height) { x, y ->
                sampled.getPixel(x, y)
            } ?: return@withContext null
            lightBackdropTintFromRgb(avg, neutralBlend, chromaAmplify)
        }.getOrNull()
    }
