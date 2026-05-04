package com.workfort.pstuian.platform

/**
 * Decodes image bytes (PNG, JPEG, WebP, GIF, HEIC where the platform supports it), fits long edge to
 * [UPLOAD_MAX_DIMENSION_PX], and re-encodes as JPEG at most [UPLOAD_MAX_JPEG_BYTES] when possible.
 */
interface ImageToJpegEncoder {

    /**
     * @param quality Preferred starting JPEG quality 1–100; implementation may reduce quality and/or dimensions
     * to satisfy [UPLOAD_MAX_JPEG_BYTES].
     */
    suspend fun encodeToJpeg(
        imageBytes: ByteArray,
        quality: Int = DEFAULT_JPEG_QUALITY,
    ): Result<ByteArray>

    companion object {
        const val DEFAULT_JPEG_QUALITY: Int = 85

        /** Maximum longer edge (px) before JPEG compression. Aspect ratio preserved; never upscales. */
        const val UPLOAD_MAX_DIMENSION_PX: Int = 1024

        /** Target maximum encoded JPEG size (500 KiB). */
        const val UPLOAD_MAX_JPEG_BYTES: Int = 500 * 1024

        internal const val MIN_JPEG_QUALITY_FOR_SIZE_CAP: Int = 35
        internal const val JPEG_QUALITY_STEP: Int = 5

        /** When quality alone fails the byte cap; longest edge multiplied by this, then JPEG retried from [quality]. */
        const val SIZE_CAP_SCALE_FACTOR: Double = 0.85

        internal const val MIN_DIMENSION_FOR_RETRY_PX: Int = 96

        /**
         * [android.graphics.BitmapFactory] decoding only: power-of-two [inSampleSize] targets edges ≤ this before
         * final resize (ImageDecoder unchanged).
         */
        internal const val INTERIM_DECODE_MAX_EDGE_PX: Int = 2048

        fun clampQuality(quality: Int): Int = quality.coerceIn(1, 100)
    }
}
