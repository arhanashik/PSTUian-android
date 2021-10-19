package com.workfort.pstuian.app.ui.common.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.util.lib.workmanager.FileDownloadWorker
import com.workfort.pstuian.util.lib.workmanager.FileUploadWorker
import com.workfort.pstuian.util.lib.workmanager.ImageCompressorWorker

/**
 *  ****************************************************************************
 *  * Created by : arhan on 19 Oct, 2021 at 2:45 PM.
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

class FileHandlerViewModel : ViewModel() {
    fun compress(context: Context, uri: Uri, fileName: String): LiveData<WorkInfo> {
        val data = workDataOf (Const.Key.URI to uri.toString(), Const.Key.NAME to fileName)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request = OneTimeWorkRequestBuilder<ImageCompressorWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        val manager = WorkManager.getInstance(context)
        manager.enqueue(request)

        return manager.getWorkInfoByIdLiveData(request.id)
    }

    fun upload(context: Context, fileName: String): LiveData<WorkInfo> {
        val data = workDataOf (Const.Key.NAME to fileName)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request = OneTimeWorkRequestBuilder<FileUploadWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        val manager = WorkManager.getInstance(context)
        manager.enqueue(request)

        return manager.getWorkInfoByIdLiveData(request.id)
    }

    fun download(context: Context, url: String, storageUri: String): LiveData<WorkInfo> {
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

        return manager.getWorkInfoByIdLiveData(request.id)
    }
}