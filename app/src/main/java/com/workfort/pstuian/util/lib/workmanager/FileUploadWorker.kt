package com.workfort.pstuian.util.lib.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.workfort.pstuian.PstuianApp
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.util.remote.RetrofitBuilder
import com.workfort.pstuian.util.remote.util.ProgressRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File


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

class FileUploadWorker(
    context: Context, workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val fileName = inputData.getString(Const.Key.NAME)
        if(fileName.isNullOrEmpty()) {
            return Result.failure()
        }

        return withContext(Dispatchers.IO) {
            uploadFile(fileName)
        }
    }

    private suspend fun uploadFile(fileName: String): Result {
        val context = PstuianApp.getBaseApplicationContext()
        val file = File(context.cacheDir, fileName)
        if(!file.exists()) {
            Result.failure()
        }
        val fileRequestBody = file.asRequestBody("image/jpeg".toMediaType())
        val requestBody = ProgressRequestBody(fileRequestBody) { bytesWritten, contentLength ->
            val progress = 100 * bytesWritten / contentLength
            Timber.e("progress : $progress")
            setProgressAsync(workDataOf(Const.Key.PROGRESS to progress))
            if (progress >= 1.0) {
                return@ProgressRequestBody
            }
        }

        val service = RetrofitBuilder.createFileHandlerApiService()
        val response = service.upload(
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
        } else Result.failure()
    }

    private fun String.toPlainTextBody() = toRequestBody("text/plain".toMediaType())
}