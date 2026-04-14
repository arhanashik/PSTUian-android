package com.workfort.pstuian.workmanager.usecase.domain

import android.content.Context
import com.workfort.pstuian.featuredomain.model.ProgressRequestState
import com.workfort.pstuian.featuredomain.model.UserType
import kotlinx.coroutines.flow.Flow

interface UploadImageUseCase {
    suspend operator fun invoke(
        context: Context,
        userType: UserType,
        fileName: String,
    ): Flow<ProgressRequestState>
}