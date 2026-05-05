package com.workfort.pstuian.util.di

import com.workfort.pstuian.util.FileUtil
import com.workfort.pstuian.util.ImageUtil
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.posix.memcpy
import platform.CoreGraphics.CGImageGetHeight
import platform.CoreGraphics.CGImageGetWidth
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.data
import platform.Foundation.writeToURL
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation

private class IosFileUtil : FileUtil {
    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override suspend fun readBytes(uri: String): Result<ByteArray> = withContext(Dispatchers.Default) {
        runCatching {
            val nsUrl = NSURL.URLWithString(uri) ?: error("Invalid URI")
            val path = nsUrl.path ?: error("Invalid file path")
            val data = NSData.create(contentsOfFile = path) ?: error("Could not read file")
            data.toByteArray()
        }
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override suspend fun writeBytes(destinationUri: String, bytes: ByteArray): Result<Unit> =
        withContext(Dispatchers.Default) {
            runCatching {
                val nsUrl = NSURL.URLWithString(destinationUri) ?: error("Invalid URI")
                val data = bytes.toNSData()
                // Locations from UIDocumentPicker (export/save-as) are security-scoped; writing without
                // startAccessing… fails silently with writeToURL == false for many destinations.
                val securityScopedAccess = nsUrl.startAccessingSecurityScopedResource()
                try {
                    // atomically:true often fails outside the app sandbox (File Provider / iCloud).
                    if (!data.writeToURL(nsUrl, false)) {
                        error("Could not write file")
                    }
                } finally {
                    if (securityScopedAccess) {
                        nsUrl.stopAccessingSecurityScopedResource()
                    }
                }
            }
        }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override suspend fun getFileName(uri: String): Result<String> = withContext(Dispatchers.Default) {
        runCatching {
            val nsUrl = NSURL.URLWithString(uri) ?: error("Invalid URI")
            val name = nsUrl.path
                ?.substringAfterLast('/')
                ?.takeIf { it.isNotBlank() }
            name ?: FileUtil.FALLBACK_DOCUMENT_NAME
        }
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private class IosImageUtil : ImageUtil {

    override suspend fun encodeToJpeg(imageBytes: ByteArray, quality: Int): Result<ByteArray> =
        withContext(Dispatchers.Default) {
            val startQuality = ImageUtil.clampQuality(quality)
            runCatching {
                val inputData = imageBytes.toNSData()
                val loaded = UIImage.imageWithData(inputData)
                    ?: error("Unsupported or corrupt image")

                val fitted =
                    loaded.scaledToLongEdgePx(ImageUtil.UPLOAD_MAX_DIMENSION_PX.toDouble())

                jpegShrinkUntilUnderCap(fitted, startQuality)
            }
        }

    /** Longest pixel edge at most [maxLongEdgePx]. */
    private fun UIImage.scaledToLongEdgePx(maxLongEdgePx: Double): UIImage {
        val (wPx, hPx) = pixelSizePx()
        val longest = max(wPx, hPx)
        if (longest <= maxLongEdgePx) return this

        val ratio = maxLongEdgePx / longest
        val nw = max(1, floor(wPx * ratio).toInt()).toDouble()
        val nh = max(1, floor(hPx * ratio).toInt()).toDouble()
        return drawRendered(nw, nh)
    }

    private fun UIImage.scaledUniformly(factor: Double): UIImage {
        require(factor > 0 && factor < 1)
        val (wPx, hPx) = pixelSizePx()
        val nw = max(1, floor(wPx * factor).toInt()).toDouble()
        val nh = max(1, floor(hPx * factor).toInt()).toDouble()
        check(nw <= wPx || nh <= hPx) {
            "Could not scale image further."
        }
        return drawRendered(nw, nh)
    }

    private fun UIImage.drawRendered(width: Double, height: Double): UIImage {
        UIGraphicsBeginImageContextWithOptions(CGSizeMake(width, height), false, 1.0)
        try {
            drawInRect(CGRectMake(0.0, 0.0, width, height))
            return checkNotNull(UIGraphicsGetImageFromCurrentImageContext()) {
                "Image render failed."
            }
        } finally {
            UIGraphicsEndImageContext()
        }
    }

    private fun UIImage.pixelSizePx(): Pair<Double, Double> {
        CGImage?.let { cgRef ->
            return Pair(
                CGImageGetWidth(cgRef).toDouble(),
                CGImageGetHeight(cgRef).toDouble(),
            )
        }
        val s = scale
        return Pair(
            size.useContents { width } * s,
            size.useContents { height } * s,
        )
    }

    private fun jpegShrinkUntilUnderCap(seed: UIImage, startQuality: Int): ByteArray {
        var candidate = seed
        while (true) {
            var q = min(startQuality, 100)
            while (q >= ImageUtil.MIN_JPEG_QUALITY_FOR_SIZE_CAP) {
                val jpegData =
                    UIImageJPEGRepresentation(candidate, q / 100.0)
                        ?: error("Could not compress image as JPEG")

                val blob = jpegData.toByteArray()
                if (blob.size <= ImageUtil.UPLOAD_MAX_JPEG_BYTES) {
                    return blob
                }
                q -= ImageUtil.JPEG_QUALITY_STEP
            }

            val (pw, ph) = candidate.pixelSizePx()
            val longEdgePx = max(pw, ph)
            check(longEdgePx >= ImageUtil.MIN_DIMENSION_FOR_RETRY_PX.toDouble()) {
                "Photo could not be reduced under 500KB while keeping usable quality."
            }

            candidate = candidate.scaledUniformly(ImageUtil.SIZE_CAP_SCALE_FACTOR)
        }
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun ByteArray.toNSData(): NSData {
    if (isEmpty()) return NSData.data()
    return usePinned { pinned ->
        NSData.create(
            bytes = pinned.addressOf(0),
            length = size.toULong(),
        )
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun NSData.toByteArray(): ByteArray {
    val len = length.toInt()
    if (len == 0) return byteArrayOf()
    val bytesPtr = bytes ?: return byteArrayOf()
    return ByteArray(len).also { out ->
        out.usePinned { pinned ->
            memcpy(pinned.addressOf(0), bytesPtr, length)
        }
    }
}

actual val platformFileImageModule: Module = module {
    single<FileUtil> { IosFileUtil() }
    single<ImageUtil> { IosImageUtil() }
}
