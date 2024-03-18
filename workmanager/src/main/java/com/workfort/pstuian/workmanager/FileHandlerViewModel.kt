package com.workfort.pstuian.workmanager

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.model.ProgressRequestState
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.workmanager.worker.FileDownloadWorker
import com.workfort.pstuian.workmanager.worker.ImageCompressorWorker
import com.workfort.pstuian.workmanager.worker.ImageUploadWorker
import com.workfort.pstuian.workmanager.worker.PdfUploadWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class FileHandlerViewModel : ViewModel() {

    private val _compressAndUploadImageState = MutableStateFlow<ProgressRequestState>(ProgressRequestState.Idle)
    val compressAndUploadImageState: StateFlow<ProgressRequestState> get() = _compressAndUploadImageState

    private val _uploadFileState = MutableStateFlow<ProgressRequestState>(ProgressRequestState.Idle)
    val uploadFileState: StateFlow<ProgressRequestState> get() = _uploadFileState

    private val _downloadFileState = MutableStateFlow<ProgressRequestState>(ProgressRequestState.Idle)
    val downloadFileState: StateFlow<ProgressRequestState> get() = _downloadFileState

    fun compressAndUploadImage(
        context: Context,
        uri: Uri,
        fileName: String,
        fileDir: File,
        userType: String,
    ) {
        viewModelScope.launch {
            _compressAndUploadImageState.value = ProgressRequestState.Idle
            compressImage(context, uri, fileName).collect { compressInfo ->
                when (compressInfo.state) {
                    WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING -> {
                        val progress = compressInfo.progress.getInt(Const.Key.PROGRESS, 0)
                        _compressAndUploadImageState.value = ProgressRequestState.Loading(progress)
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        val file = File(fileDir, fileName)
                        if (file.exists().not()) {
                            _compressAndUploadImageState.value = ProgressRequestState.Error(
                                "Failed to resize the image. Please try again."
                            )
                        } else {
                            uploadImage(context, userType, fileName).collect { uploadInfo ->
                                when (uploadInfo.state) {
                                    WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING -> {
                                        val progress = compressInfo.progress.getInt(Const.Key.PROGRESS, 0)
                                        _compressAndUploadImageState.value = ProgressRequestState.Loading(progress)
                                    }
                                    WorkInfo.State.SUCCEEDED -> {
                                        val data = uploadInfo.outputData.getString(Const.Key.URL)
                                        if (data.isNullOrEmpty()) {
                                            _compressAndUploadImageState.value = ProgressRequestState.Error(
                                                "Failed to upload the image. Please try again."
                                            )
                                        } else {
                                            _compressAndUploadImageState.value = ProgressRequestState.Success(data)
                                        }
                                    }
                                    WorkInfo.State.FAILED,
                                    WorkInfo.State.CANCELLED,
                                    WorkInfo.State.BLOCKED -> {
                                        _compressAndUploadImageState.value = ProgressRequestState.Error(
                                            "Failed to upload the image. Please try again."
                                        )
                                    }
                                }
                            }
                        }
                        RequestState.Success<Unit>()
                    }
                    WorkInfo.State.FAILED, WorkInfo.State.CANCELLED, WorkInfo.State.BLOCKED -> {
                        _compressAndUploadImageState.value = ProgressRequestState.Error(
                            "Failed to resize the image. Please try again."
                        )
                    }
                }
            }
        }
    }

    private fun compressImage(context: Context, uri: Uri, fileName: String): Flow<WorkInfo> {
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

        return manager.getWorkInfoByIdFlow(request.id)
    }

    // the image should be inside the cache directory
    private fun uploadImage(context: Context, userType: String, fileName: String): Flow<WorkInfo> {
        val data = workDataOf (
            Const.Key.USER_TYPE to  userType,
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

        return manager.getWorkInfoByIdFlow(request.id)
    }

    fun uploadPdf(context: Context, pdfUri: Uri, fileName: String) {
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

        viewModelScope.launch {
            manager.getWorkInfoByIdFlow(request.id).collect { workInfo ->
                when (workInfo.state) {
                    WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING -> {
                        val progress = workInfo.progress.getInt(Const.Key.PROGRESS, 0)
                        _uploadFileState.value = ProgressRequestState.Loading(progress)
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        val link = workInfo.outputData.getString(Const.Key.URL)
                        if(link.isNullOrEmpty()) {
                            _uploadFileState.value = ProgressRequestState.Error(
                                "Failed to upload. Please try again."
                            )
                        } else {
                            _uploadFileState.value = ProgressRequestState.Success(link)
                        }
                    }
                    WorkInfo.State.FAILED,
                    WorkInfo.State.CANCELLED,
                    WorkInfo.State.BLOCKED -> {
                        val msg = workInfo.outputData.getString(Const.Key.MESSAGE)
                        _uploadFileState.value = ProgressRequestState.Error(msg)
                    }
                }
            }
        }
    }

    fun download(context: Context, url: String, storageUri: String) {
        val data = workDataOf (Const.Key.URL to url, Const.Key.URI to storageUri)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request = OneTimeWorkRequestBuilder<FileDownloadWorker>()
                .setInputData(data)
                .setConstraints(constraints)
                .build()

        val manager = WorkManager.getInstance(context)
        manager.enqueue(request)

        viewModelScope.launch {
            manager.getWorkInfoByIdFlow(request.id).collect { workInfo ->
                when (workInfo.state) {
                    WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING -> {
                        val progress = workInfo.progress.getInt(Const.Key.PROGRESS, 0)
                        _downloadFileState.value = ProgressRequestState.Loading(progress)
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        _downloadFileState.value = ProgressRequestState.Success("Download Successfully")
                    }
                    WorkInfo.State.FAILED,
                    WorkInfo.State.CANCELLED,
                    WorkInfo.State.BLOCKED -> {
                        _downloadFileState.value = ProgressRequestState.Error(
                            "Failed to download. Please try again."
                        )
                    }
                }
            }
        }
    }
}