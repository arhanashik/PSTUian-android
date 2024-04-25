package com.workfort.pstuian.workmanager.usecase.infrastructure

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.model.ProgressRequestState
import com.workfort.pstuian.workmanager.usecase.domain.DownloadPdfUseCase
import com.workfort.pstuian.workmanager.worker.FileDownloadWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class DownloadPdfUseCaseImpl : DownloadPdfUseCase {
    override suspend fun invoke(
        context: Context,
        url: String,
        storageUri: String
    ): Flow<ProgressRequestState> {
        val data = workDataOf (
            Const.Key.URL to url,
            Const.Key.URI to storageUri,
        )
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request = OneTimeWorkRequestBuilder<FileDownloadWorker>()
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
                    ProgressRequestState.Success("Downloaded Successfully")
                }
                WorkInfo.State.FAILED,
                WorkInfo.State.CANCELLED,
                WorkInfo.State.BLOCKED -> {
                    ProgressRequestState.Error("Failed to download. Please try again.")
                }
            }
        }
    }
}