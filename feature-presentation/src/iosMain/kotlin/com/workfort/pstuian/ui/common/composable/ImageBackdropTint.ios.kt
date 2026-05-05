package com.workfort.pstuian.ui.common.composable

import androidx.compose.ui.graphics.Color
import coil3.BitmapImage
import coil3.Image
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Color as SkiaColor

internal actual suspend fun extractBackdropTintFromImage(
    image: Image,
    neutralBlend: Float,
    chromaAmplify: Float,
): Color? =
    withContext(Dispatchers.Default) {
        runCatching {
            val (w, h) =
                when (image) {
                    is BitmapImage -> {
                        val src = image.bitmap
                        backdropSampleDimensions(src.width, src.height)
                    }
                    else -> backdropSampleDimensions(image)
                }
            if (w <= 0 || h <= 0) return@withContext null
            val bmp = image.toBitmap(w, h)
            val avg = averageInterestingRgb(bmp.width, bmp.height) { x, y ->
                val c = bmp.getColor(x, y)
                val a = SkiaColor.getA(c)
                val r = SkiaColor.getR(c)
                val g = SkiaColor.getG(c)
                val b = SkiaColor.getB(c)
                (a shl 24) or (r shl 16) or (g shl 8) or b
            } ?: return@withContext null
            lightBackdropTintFromRgb(avg, neutralBlend, chromaAmplify)
        }.getOrNull()
    }
