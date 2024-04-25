package com.workfort.pstuian.workmanager.usecase.domain

import android.content.Context
import com.workfort.pstuian.model.ProgressRequestState
import kotlinx.coroutines.flow.Flow

interface DownloadPdfUseCase {
    suspend operator fun invoke(
        context: Context,
        url: String,
        storageUri: String
    ): Flow<ProgressRequestState>
}