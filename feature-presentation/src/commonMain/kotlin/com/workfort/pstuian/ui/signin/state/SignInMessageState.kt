package com.workfort.pstuian.ui.signin.state

import com.workfort.pstuian.featuredomain.model.BatchEntity
import com.workfort.pstuian.featuredomain.model.FacultyEntity

sealed interface SignInMessageState {
    data class Success(val message: String) : SignInMessageState
    data class Error(val message: String) : SignInMessageState
    data class FacultySelection(
        val faculties: List<FacultyEntity>,
        val selectedFacultyId: Int?,
        val onSaveAndContinue: (FacultyEntity?) -> Unit,
    ) : SignInMessageState
    data class BatchSelection(
        val batches: List<BatchEntity>,
        val selectedBatchId: Int?,
        val onSaveAndContinue: (BatchEntity?) -> Unit,
    ) : SignInMessageState
}
