package com.workfort.pstuian.workmanager.usecase.domain

import android.content.Context
import android.net.Uri
import com.workfort.pstuian.model.ProgressRequestState
import kotlinx.coroutines.flow.Flow

interface CompressImageUseCase {
    suspend operator fun invoke(
        context: Context,
        uri: Uri,
        fileName: String,
    ): Flow<ProgressRequestState>
}