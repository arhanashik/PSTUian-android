package com.workfort.pstuian.ui.common.composable

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import coil3.Image
import kotlin.math.max

private const val SAMPLE_WIDTH_PX = 72

/**
 * Defaults for tiny remote assets (e.g. faculty icons): heavier wash toward neutral so thumbnails
 * stay calm. Raise blend for subtler hue; lower for bolder backdrops.
 */
internal const val DEFAULT_BACKDROP_NEUTRAL_BLEND = 0.76f

/** Default chroma amplification before blending toward neutral for small raster/icon sources. */
internal const val DEFAULT_BACKDROP_CHROMA_AMPLIFY = 1.16f

/** Fullscreen image preview: keep dominant photo hues visible. */
internal const val IMAGE_PREVIEW_NEUTRAL_BLEND = 0.35f
internal const val IMAGE_PREVIEW_CHROMA_AMPLIFY = 1.33f

/**
 * Profile header from avatars: more saturation than defaults; tweak [PROFILE_HEADER_NEUTRAL_BLEND] /
 * [PROFILE_HEADER_CHROMA_AMPLIFY] for liveliness vs calm.
 */
internal const val PROFILE_HEADER_NEUTRAL_BLEND = 0.39f
internal const val PROFILE_HEADER_CHROMA_AMPLIFY = 1.34f

private val BACKDROP_BLEND_NEUTRAL = Color(red = 1f, green = 252f / 255f, blue = 251f / 255f)

/**
 * Grid-samples [pixelAt] (ARGB 0xAARRGGBB), skipping near-transparent, very dark, and very light pixels.
 */
internal fun averageInterestingRgb(
    width: Int,
    height: Int,
    pixelAt: (x: Int, y: Int) -> Int,
): Triple<Int, Int, Int>? {
    if (width <= 0 || height <= 0) return null
    val stepX = max(1, width / 12)
    val stepY = max(1, height / 12)
    var rSum = 0L
    var gSum = 0L
    var bSum = 0L
    var count = 0L
    var y = 0
    while (y < height) {
        var x = 0
        while (x < width) {
            val p = pixelAt(x, y)
            val a = (p ushr 24) and 0xFF
            if (a >= 56) {
                val rv = (p ushr 16) and 0xFF
                val gv = (p ushr 8) and 0xFF
                val bv = p and 0xFF
                val lum = 0.299 * rv + 0.587 * gv + 0.114 * bv
                if (lum in 8.0..252.0) {
                    rSum += rv
                    gSum += gv
                    bSum += bv
                    count++
                }
            }
            x += stepX
        }
        y += stepY
    }
    if (count == 0L) {
        val cx = width / 2
        val cy = height / 2
        val p = pixelAt(cx, cy)
        val rv = (p ushr 16) and 0xFF
        val gv = (p ushr 8) and 0xFF
        val bv = p and 0xFF
        return Triple(rv, gv, bv)
    }
    return Triple(
        (rSum / count).toInt().coerceIn(0, 255),
        (gSum / count).toInt().coerceIn(0, 255),
        (bSum / count).toInt().coerceIn(0, 255),
    )
}

/** Turns a grid-sampled RGB triple into a light pastel suitable for surfaces behind content. */
internal fun lightBackdropTintFromRgb(
    rgb: Triple<Int, Int, Int>,
    neutralBlend: Float = DEFAULT_BACKDROP_NEUTRAL_BLEND,
    chromaAmplify: Float = DEFAULT_BACKDROP_CHROMA_AMPLIFY,
): Color {
    val (rIn, gIn, bIn) = amplifyChromaFromGray(rgb.first, rgb.second, rgb.third, chromaAmplify)
    val base = Color(rIn / 255f, gIn / 255f, bIn / 255f)
    return lerp(base, BACKDROP_BLEND_NEUTRAL, neutralBlend)
}

private fun amplifyChromaFromGray(rIn: Int, gIn: Int, bIn: Int, factor: Float): Triple<Int, Int, Int> {
    val lf = (0.299 * rIn + 0.587 * gIn + 0.114 * bIn).toFloat()
    fun out(c: Int): Int =
        (lf + (c - lf) * factor).toInt().coerceIn(0, 255)
    return Triple(out(rIn), out(gIn), out(bIn))
}

/**
 * Downscale a Coil [Image], sample pixels, and return a light backdrop [Color].
 * Use [DEFAULT_BACKDROP_*] for icon-like sources; pass custom blend/chroma for photos (see
 * [IMAGE_PREVIEW_NEUTRAL_BLEND], [PROFILE_HEADER_NEUTRAL_BLEND], etc.).
 */
internal expect suspend fun extractBackdropTintFromImage(
    image: Image,
    neutralBlend: Float = DEFAULT_BACKDROP_NEUTRAL_BLEND,
    chromaAmplify: Float = DEFAULT_BACKDROP_CHROMA_AMPLIFY,
): Color?

/** Profile header: same pipeline as [extractBackdropTintFromImage] with portrait-friendly presets. */
internal suspend fun extractProfileHeaderBackground(image: Image): Color? =
    extractBackdropTintFromImage(
        image,
        neutralBlend = PROFILE_HEADER_NEUTRAL_BLEND,
        chromaAmplify = PROFILE_HEADER_CHROMA_AMPLIFY,
    )

internal fun backdropSampleDimensions(image: Image): Pair<Int, Int> =
    backdropSampleDimensions(image.width, image.height)

/** Same sizing as [backdropSampleDimensions] for known pixel dimensions (e.g. Android [Bitmap]). */
internal fun backdropSampleDimensions(intrinsicWidth: Int, intrinsicHeight: Int): Pair<Int, Int> {
    val w = SAMPLE_WIDTH_PX
    val h =
        if (intrinsicWidth > 0 && intrinsicHeight > 0) {
            max(1, intrinsicHeight * w / intrinsicWidth)
        } else {
            SAMPLE_WIDTH_PX
        }
    return w to h
}
