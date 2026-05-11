package com.workfort.pstuian.ui.signin.state

import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.Faculty

sealed interface SignInMessageState {
    data class Success(val message: String) : SignInMessageState
    data class Error(val message: String) : SignInMessageState
    data class FacultySelection(
        val faculties: List<Faculty>,
        val selectedFacultyId: Int?,
        val onSaveAndContinue: (Faculty?) -> Unit,
    ) : SignInMessageState
    data class BatchSelection(
        val batches: List<Batch>,
        val selectedBatchId: Int?,
        val onSaveAndContinue: (Batch?) -> Unit,
    ) : SignInMessageState
}