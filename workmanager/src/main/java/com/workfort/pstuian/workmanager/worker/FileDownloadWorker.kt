package com.workfort.pstuian.workmanager.worker

import android.content.Context
import android.net.Uri
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.workfort.pstuian.appconstant.Const
import okio.FileNotFoundException
import okio.IOException
import java.io.FileOutputStream
import java.net.URL

class FileDownloadWorker(
    private val context: Context, workerParams: WorkerParameters
): Worker(context, workerParams) {
    override fun doWork(): Result {
        val downloadUrl = inputData.getString(Const.Key.URL)
        val storageUriStr = inputData.getString(Const.Key.URI)
        if(downloadUrl.isNullOrEmpty() || storageUriStr.isNullOrEmpty()) {
            return Result.failure()
        }

        return downloadFile(context, downloadUrl, storageUriStr)
    }

    private fun downloadFile(
        context: Context,
        downloadUrl: String,
        storageUriStr: String
    ): Result {
        val url = URL(downloadUrl)
        val connection = url.openConnection()
        connection.connect()
        val length = connection.contentLengthLong
        url.openStream().use { input ->
            try {
                val contentResolver = context.contentResolver
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
                e.printStackTrace()
                return Result.failure()
            } catch (e: IOException) {
                e.printStackTrace()
                return Result.failure()
            } catch (e: NullPointerException) {
                e.printStackTrace()
                return Result.failure()
            }
        }

        return Result.success()
    }
}