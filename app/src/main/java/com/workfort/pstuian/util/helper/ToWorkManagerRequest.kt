package com.workfort.pstuian.util.helper

import android.content.Context
import android.net.Uri
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import com.workfort.pstuian.featuredomain.appconstant.Const
import com.workfort.pstuian.workmanager.worker.FileDownloadWorker
import com.workfort.pstuian.workmanager.worker.ImageUploadWorker
import com.workfort.pstuian.workmanager.worker.PdfUploadWorker

object ToWorkManagerRequest {
    fun uploadImage(context: Context, uri: Uri): OneTimeWorkRequest {
        val data = workDataOf(
            Const.Key.URI to uri.toString(),
        )
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        return OneTimeWorkRequestBuilder<ImageUploadWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()
    }

    fun uploadPdf(context: Context, uri: Uri): OneTimeWorkRequest {
        val data = workDataOf(
            Const.Key.URI to uri.toString(),
        )
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        return OneTimeWorkRequestBuilder<PdfUploadWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()
    }

    fun downloadPdf(context: Context, url: String, uri: Uri): OneTimeWorkRequest {
        val data = workDataOf(
            Const.Key.URL to url,
            Const.Key.URI to uri.toString(),
        )
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        return OneTimeWorkRequestBuilder<FileDownloadWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()
    }
}
