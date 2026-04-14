package com.workfort.pstuian.workmanager.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.data.remote.service.FileHandlerApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
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
 *  ****************************************************************************
 */

class ImageUploadWorker(
    private val context: Context, workerParams: WorkerParameters
): CoroutineWorker(context, workerParams), KoinComponent {
    override suspend fun doWork(): Result {
        val userType = inputData.getString(Const.Key.USER_TYPE)
        val fileName = inputData.getString(Const.Key.NAME)
        if(userType.isNullOrEmpty() || fileName.isNullOrEmpty()) {
            return Result.failure()
        }

        return withContext(Dispatchers.IO) {
            upload(context, userType, fileName)
        }
    }

    private suspend fun upload(context: Context, userType: String, fileName: String): Result {
        val file = File(context.cacheDir, fileName)
        if(!file.exists()) {
            return Result.failure()
        }
        val fileBytes = file.readBytes()

        val service = get<FileHandlerApiService>()
        val response = service.uploadImage(
            userType = userType,
            filename = fileName,
            fileBytes = fileBytes
        )

        return if(response.success) {
            val data = workDataOf(Const.Key.URL to response.data)
            Result.success(data)
        } else {
            val data = workDataOf(Const.Key.MESSAGE to response.message)
            Result.failure(data)
        }
    }
}
