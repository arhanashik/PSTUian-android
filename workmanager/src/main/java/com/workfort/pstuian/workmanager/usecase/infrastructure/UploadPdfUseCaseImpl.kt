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
import com.workfort.pstuian.workmanager.usecase.domain.UploadPdfUseCase
import com.workfort.pstuian.workmanager.worker.PdfUploadWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UploadPdfUseCaseImpl : UploadPdfUseCase {
    override suspend fun invoke(
        context: Context,
        pdfUri: Uri,
        fileName: String,
    ): Flow<ProgressRequestState> {
        val data = workDataOf (
            Const.Key.URI to pdfUri.toString(),
            Const.Key.NAME to fileName
        )
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request = OneTimeWorkRequestBuilder<PdfUploadWorker>()
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
                    val link = workInfo.outputData.getString(Const.Key.URL)
                    if(link.isNullOrEmpty()) {
                        ProgressRequestState.Error(error = "Failed to upload. Please try again.")
                    } else {
                        ProgressRequestState.Success(link)
                    }
                }
                WorkInfo.State.FAILED,
                WorkInfo.State.CANCELLED,
                WorkInfo.State.BLOCKED -> {
                    ProgressRequestState.Error(
                        error = workInfo.outputData.getString(Const.Key.MESSAGE),
                    )
                }
            }
        }
    }
}