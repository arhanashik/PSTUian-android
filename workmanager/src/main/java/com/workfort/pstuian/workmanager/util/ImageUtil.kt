package com.workfort.pstuian.workmanager.util

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

object ImageUtil {
    fun compress(image: Bitmap, maxSize: Int): ByteArray {
        val outputStream = ByteArrayOutputStream()
        var quality = 100
        image.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        while (outputStream.size() / 1024 > maxSize) {
            outputStream.reset()
            quality -= 10
            image.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        }
        if (!image.isRecycled) {
            try {
                image.recycle()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return outputStream.toByteArray()
    }
}