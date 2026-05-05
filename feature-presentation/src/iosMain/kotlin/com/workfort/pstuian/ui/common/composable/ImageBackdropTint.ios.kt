package com.workfort.pstuian.ui.common.composable

import androidx.compose.ui.graphics.Color
import coil3.BitmapImage
import coil3.Image
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Color as SkiaColor

private fun toArgbInt(skiaColor: Int): Int {
    val a = SkiaColor.getA(skiaColor)
    val rRaw = SkiaColor.getR(skiaColor)
    val gRaw = SkiaColor.getG(skiaColor)
    val bRaw = SkiaColor.getB(skiaColor)
    // Skia-backed bitmaps on iOS can expose premultiplied RGB values.
    // Convert back to straight alpha so tint extraction matches Android behavior.
    val (r, g, b) =
        if (a in 1..254) {
            Triple(
                (rRaw * 255 / a).coerceIn(0, 255),
                (gRaw * 255 / a).coerceIn(0, 255),
                (bRaw * 255 / a).coerceIn(0, 255),
            )
        } else {
            Triple(rRaw, gRaw, bRaw)
        }
    return (a shl 24) or (r shl 16) or (g shl 8) or b
}

internal actual suspend fun extractBackdropTintFromImage(
    image: Image,
    neutralBlend: Float,
    chromaAmplify: Float,
): Color? =
    withContext(Dispatchers.Default) {
        runCatching {
            val avg =
                when (image) {
                    is BitmapImage -> {
                        val src = image.bitmap
                        if (src.width <= 0 || src.height <= 0) return@withContext null
                        val (sampleW, sampleH) = backdropSampleDimensions(src.width, src.height)
                        averageInterestingRgb(sampleW, sampleH) { x, y ->
                            val sx = (x * src.width / sampleW).coerceIn(0, src.width - 1)
                            val sy = (y * src.height / sampleH).coerceIn(0, src.height - 1)
                            toArgbInt(src.getColor(sx, sy))
                        }
                    }
                    else -> {
                        val (w, h) = backdropSampleDimensions(image)
                        if (w <= 0 || h <= 0) return@withContext null
                        val bmp = image.toBitmap(w, h)
                        averageInterestingRgb(bmp.width, bmp.height) { x, y ->
                            toArgbInt(bmp.getColor(x, y))
                        }
                    }
                } ?: return@withContext null
            lightBackdropTintFromRgb(avg, neutralBlend, chromaAmplify)
        }.getOrNull()
    }
