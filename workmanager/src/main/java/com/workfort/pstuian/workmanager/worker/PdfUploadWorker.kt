package com.workfort.pstuian.workmanager.worker

import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.networking.service.RetrofitBuilder
import com.workfort.pstuian.workmanager.ProgressRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.FileNotFoundException
import okio.IOException
import java.io.File
import java.io.FileOutputStream

class PdfUploadWorker(
    private val context: Context, workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val pdfUriStr = inputData.getString(Const.Key.URI)
        val fileName = inputData.getString(Const.Key.NAME)
        if(pdfUriStr.isNullOrEmpty() || fileName.isNullOrEmpty()) {
            return Result.failure()
        }

        return withContext(Dispatchers.IO) {
            upload(context, pdfUriStr, fileName)
        }
    }

    private suspend fun upload(context: Context, pdfUriStr: String, fileName: String): Result {
        val file = createTempFile(context, pdfUriStr, fileName)?: return Result.failure()
        val fileRequestBody = file.asRequestBody("application/pdf".toMediaType())
        val requestBody = ProgressRequestBody(fileRequestBody) { bytesWritten, contentLength ->
            val progress = 100 * bytesWritten / contentLength
            setProgressAsync(workDataOf(Const.Key.PROGRESS to progress))
            if (progress >= 1.0) {
                return@ProgressRequestBody
            }
        }

        val service = RetrofitBuilder.createFileHandlerApiService()
        val response = service.uploadPdf(
            filename = fileName.toPlainTextBody(),
            file = MultipartBody.Part.createFormData(
                name = "file",
                filename = fileName,
                body = requestBody
            )
        )

        return if(response.success) {
            val data = workDataOf(Const.Key.URL to response.data)
            Result.success(data)
        } else {
            val data = workDataOf(Const.Key.MESSAGE to response.message)
            Result.failure(data)
        }
    }

    private fun createTempFile(context: Context, pdfUriStr: String, fileName: String) : File? {
        val contentResolver = context.contentResolver
        try {
            val uri = Uri.parse(pdfUriStr)
            val tempFile = File(context.cacheDir, fileName)
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(tempFile).use { outputStream ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var n = inputStream.read(buffer)
                    while (n != -1) {
                        outputStream.write(buffer, 0, n)
                        n = inputStream.read(buffer)
                    }
                }
            }
            return if(tempFile.exists() && tempFile.length() > 0) tempFile else null
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return null
        }
    }

    private fun String.toPlainTextBody() = toRequestBody("text/plain".toMediaType())
}