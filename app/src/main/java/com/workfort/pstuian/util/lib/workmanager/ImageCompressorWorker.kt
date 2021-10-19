package com.workfort.pstuian.util.lib.workmanager

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.workfort.pstuian.PstuianApp
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.util.helper.ImageUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.FileNotFoundException
import okio.IOException
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream


/**
 *  ****************************************************************************
 *  * Created by : arhan on 19 Oct, 2021 at 2:48 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1. Compress an given image
 *  * 2. If successful, the data contains the file name.
 *  * 3. The file should be found under File(context.cacheDir, fileName)
 *  *
 *  * Last edited by : arhan on 10/19/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class ImageCompressorWorker(
    context: Context, workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val uriStr = inputData.getString(Const.Key.URI)
        val tempFile = inputData.getString(Const.Key.NAME)
        if(uriStr.isNullOrEmpty() || tempFile.isNullOrEmpty()) {
            return Result.failure()
        }

        return withContext(Dispatchers.IO) {
            compress(uriStr, tempFile)
        }
    }

    private fun compress(uriStr: String, tempFileName: String, ): Result {
        val context = PstuianApp.getBaseApplicationContext()
        val contentResolver = context.contentResolver
        try {
            val uri = Uri.parse(uriStr)
            val tempFile = File(context.cacheDir, tempFileName)
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val compressed = ImageUtil.compress(bitmap, 512)
                FileOutputStream(tempFile).use { outputStream ->
                    outputStream.write(compressed)
                    outputStream.flush()
                }
            }
            val data = workDataOf(Const.Key.NAME to tempFileName)
            return if(tempFile.length() > 0) Result.success(data) else Result.failure()
        } catch (e: FileNotFoundException) {
            Timber.e(e)
            return Result.failure()
        } catch (e: IOException) {
            Timber.e(e)
            return Result.failure()
        } catch (e: NullPointerException) {
            Timber.e(e)
            return Result.failure()
        }
    }
}