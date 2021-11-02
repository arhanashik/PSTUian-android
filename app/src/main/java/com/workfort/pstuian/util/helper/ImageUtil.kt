package com.workfort.pstuian.util.helper

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream


/**
 *  ****************************************************************************
 *  * Created by : arhan on 20 Oct, 2021 at 2:20 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/20/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

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