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
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.workmanager.usecase.domain.UploadImageUseCase
import com.workfort.pstuian.workmanager.worker.ImageUploadWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UploadImageUseCaseImpl : UploadImageUseCase {
    override suspend fun invoke(
        context: Context,
        userType: UserType,
        fileName: String,
    ): Flow<ProgressRequestState> {
        val data = workDataOf (
            Const.Key.USER_TYPE to userType.type,
            Const.Key.NAME to fileName
        )
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request = OneTimeWorkRequestBuilder<ImageUploadWorker>()
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
                    val url = workInfo.outputData.getString(Const.Key.URL)
                    if(url.isNullOrEmpty()) {
                        ProgressRequestState.Error(error = "Failed to upload. Please try again.")
                    } else {
                        ProgressRequestState.Success(url)
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