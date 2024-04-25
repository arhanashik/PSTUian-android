package com.workfort.pstuian.workmanager.usecase.infrastructure

import android.content.Context
import android.net.Uri
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.model.ProgressRequestState
import com.workfort.pstuian.workmanager.usecase.domain.CompressImageUseCase
import com.workfort.pstuian.workmanager.worker.ImageCompressorWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File


class CompressImageUseCaseImpl : CompressImageUseCase {
    override suspend fun invoke(
        context: Context,
        uri: Uri,
        fileName: String,
    ): Flow<ProgressRequestState> {
        val data = workDataOf (
            Const.Key.URI to uri.toString(),
            Const.Key.NAME to fileName,
            Const.Key.MAX_SIZE to 200, //200kb
        )
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request = OneTimeWorkRequestBuilder<ImageCompressorWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        val manager = WorkManager.getInstance(context)
        manager.enqueue(request)

        return manager.getWorkInfoByIdFlow(request.id).map { workInfo ->
            when (workInfo.state) {
                WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING -> {
                    val progress = workInfo.progress.getInt(Const.Key.PROGRESS, 0)
                    ProgressRequestState.Loading(progress)
                }
                WorkInfo.State.SUCCEEDED -> {
                    // if successful, the file should exist in cache dir
                    val file = File(context.cacheDir, fileName)
                    if (file.exists()) {
                        ProgressRequestState.Success("Image resized successfully")
                    } else {
                        ProgressRequestState.Error(
                            error = "Failed to resize the image. Please try again."
                        )
                    }
                }
                WorkInfo.State.FAILED,
                WorkInfo.State.CANCELLED,
                WorkInfo.State.BLOCKED -> {
                    ProgressRequestState.Error(
                        error = "Failed to resize the image. Please try again.",
                    )
                }
            }
        }
    }
}