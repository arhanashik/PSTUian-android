package com.workfort.pstuian.di

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import androidx.core.net.toUri
import com.workfort.pstuian.platform.ImageToJpegEncoder
import com.workfort.pstuian.platform.UriBytesReader
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

private class AndroidUriBytesReader(
    private val context: Context,
) : UriBytesReader {

    override suspend fun readBytes(uri: String): Result<ByteArray> = withContext(Dispatchers.IO) {
        runCatching {
            val parsed = uri.toUri()
            context.contentResolver.openInputStream(parsed)?.use { stream -> stream.readBytes() }
                ?: error("Could not open the selected image")
        }
    }
}

private class AndroidImageToJpegEncoder(
    private val context: Context,
) : ImageToJpegEncoder {

    override suspend fun encodeToJpeg(imageBytes: ByteArray, quality: Int): Result<ByteArray> =
        withContext(Dispatchers.IO) {
            runCatching {
                val decoded =
                    decodeBitmap(context, imageBytes) ?: error("Unsupported or corrupt image")

                val working =
                    decoded.scaledToMaxDimension(ImageToJpegEncoder.UPLOAD_MAX_DIMENSION_PX)
                shrinkAndEncodeUntilUnderByteCap(
                    bitmap = working,
                    startQuality = ImageToJpegEncoder.clampQuality(quality),
                )
            }
        }

    /** Takes ownership of [bitmap] (always recycled before return). */
    private fun shrinkAndEncodeUntilUnderByteCap(bitmap: Bitmap, startQuality: Int): ByteArray {
        var current = bitmap
        try {
            while (true) {
                var q = min(startQuality, 100)
                while (q >= ImageToJpegEncoder.MIN_JPEG_QUALITY_FOR_SIZE_CAP) {
                    val jpeg = current.compressToJpeg(q)
                    if (jpeg.size <= ImageToJpegEncoder.UPLOAD_MAX_JPEG_BYTES) {
                        current.recycle()
                        return jpeg
                    }
                    q -= ImageToJpegEncoder.JPEG_QUALITY_STEP
                }

                val longEdge = max(current.width, current.height)

                check(longEdge >= ImageToJpegEncoder.MIN_DIMENSION_FOR_RETRY_PX) {
                    "Photo could not be reduced under 500KB while keeping usable quality."
                }

                val scaled = current.scaledUniformly(ImageToJpegEncoder.SIZE_CAP_SCALE_FACTOR)
                current.recycle()
                current = scaled
            }
        } catch (t: Throwable) {
            current.recycle()
            throw t
        }
    }

    /** Copies this bitmap resized by [factor]; caller must recycle the receiver. */
    private fun Bitmap.scaledUniformly(factor: Double): Bitmap {
        require(factor > 0 && factor < 1)
        val nw = kotlin.math.floor(width.toDouble() * factor).toInt().coerceAtLeast(1)
        val nh = kotlin.math.floor(height.toDouble() * factor).toInt().coerceAtLeast(1)

        check(nw < width || nh < height) {
            "Could not scale image further."
        }

        return Bitmap.createScaledBitmap(this, nw, nh, true)
            ?: error("Bitmap.createScaledBitmap failed")
    }

    private fun decodeBitmap(ctx: Context, bytes: ByteArray): Bitmap? {
        decodeWithBitmapFactory(bytes)?.let { return it }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val tmp = File.createTempFile("img_decode_", ".img", ctx.cacheDir)
            try {
                tmp.writeBytes(bytes)
                return ImageDecoder.decodeBitmap(ImageDecoder.createSource(tmp)) { decoder, _, _ ->
                    decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                }
            } catch (_: Throwable) {
                return null
            } finally {
                tmp.delete()
            }
        }
        return null
    }

    private fun decodeWithBitmapFactory(bytes: ByteArray): Bitmap? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

        var sampleSize = 1
        val interim = ImageToJpegEncoder.INTERIM_DECODE_MAX_EDGE_PX
        while (bounds.outWidth / sampleSize > interim || bounds.outHeight / sampleSize > interim) {
            sampleSize *= 2
        }
        val options = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
    }
}

/**
 * Fits inside a box whose longer edge is at most [maxPx] px (never upscales).
 * Recycles receiver when producing a scaled replacement.
 */
private fun Bitmap.scaledToMaxDimension(maxPx: Int): Bitmap {
    if (width <= 0 || height <= 0) error("Invalid bitmap dimensions")
    val longest = max(width, height).toDouble().coerceAtLeast(1.0)
    if (longest <= maxPx) return this

    val ratio = maxPx / longest
    val nw = max(1, (width.toDouble() * ratio).roundToInt())
    val nh = max(1, (height.toDouble() * ratio).roundToInt())
    val out = Bitmap.createScaledBitmap(this, nw, nh, true)
        ?: error("Bitmap.createScaledBitmap failed")
    if (this !== out) recycle()
    return out
}

private fun Bitmap.compressToJpeg(quality: Int): ByteArray {
    ByteArrayOutputStream().use { os ->
        val ok = compress(Bitmap.CompressFormat.JPEG, quality, os)
        if (!ok) error("Could not compress image as JPEG")
        return os.toByteArray()
    }
}

actual val platformPresentationExtrasModule: Module = module {
    single<UriBytesReader> { AndroidUriBytesReader(androidContext()) }
    single<ImageToJpegEncoder> { AndroidImageToJpegEncoder(androidContext()) }
}
