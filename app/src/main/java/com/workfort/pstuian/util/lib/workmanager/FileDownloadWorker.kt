package com.workfort.pstuian.util.lib.workmanager

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.workfort.pstuian.PstuianApp
import com.workfort.pstuian.app.data.local.constant.Const
import okio.FileNotFoundException
import okio.IOException
import timber.log.Timber
import java.io.FileOutputStream
import java.lang.NullPointerException
import java.net.URL

/**
 *  ****************************************************************************
 *  * Created by : arhan on 19 Oct, 2021 at 2:48 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/19/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class FileDownloadWorker(
    context: Context, workerParams: WorkerParameters
): Worker(context, workerParams) {
    override fun doWork(): Result {
        val downloadUrl = inputData.getString(Const.Key.URL)
        val storageUriStr = inputData.getString(Const.Key.URI)
        if(downloadUrl.isNullOrEmpty() || storageUriStr.isNullOrEmpty()) {
            return Result.failure()
        }

        return downloadFile(downloadUrl, storageUriStr)
    }

    private fun downloadFile(downloadUrl: String, storageUriStr: String): Result {
        val url = URL(downloadUrl)
        val connection = url.openConnection()
        connection.connect()
        val length = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            connection.contentLengthLong else connection.contentLength.toLong()
        url.openStream().use { input ->
            try {
                val contentResolver = PstuianApp.getBaseApplicationContext().contentResolver
                val uri = Uri.parse(storageUriStr)
                contentResolver.openFileDescriptor(uri, "w")?.use { descriptor ->
                    FileOutputStream(descriptor.fileDescriptor).use { outputStream ->
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var bytesRead = input.read(buffer)
                        var bytesCopied = 0L
                        while (bytesRead >= 0) {
                            outputStream.write(buffer, 0, bytesRead)
                            bytesCopied += bytesRead
                            val progress = (bytesCopied/length)*100
                            setProgressAsync(workDataOf(Const.Key.PROGRESS to progress))
                            bytesRead = input.read(buffer)
                        }
                    }
                }
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

        return Result.success()
    }
}