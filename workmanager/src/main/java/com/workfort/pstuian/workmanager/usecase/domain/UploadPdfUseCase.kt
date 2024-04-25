package com.workfort.pstuian.workmanager.usecase.domain

import android.content.Context
import android.net.Uri
import com.workfort.pstuian.model.ProgressRequestState
import kotlinx.coroutines.flow.Flow

interface UploadPdfUseCase {
    suspend operator fun invoke(
        context: Context,
        pdfUri: Uri,
        fileName: String,
    ): Flow<ProgressRequestState>
}