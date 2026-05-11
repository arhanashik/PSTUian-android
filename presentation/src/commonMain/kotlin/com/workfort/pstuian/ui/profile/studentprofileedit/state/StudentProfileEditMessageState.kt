package com.workfort.pstuian.ui.profile.studentprofileedit.state

import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.Faculty

sealed interface StudentProfileEditMessageState {
    data class Loading(val cancelable: Boolean = false) : StudentProfileEditMessageState
    data class FacultySelection(
        val faculties: List<Faculty>,
        val selectedFacultyId: Int?,
        val onSaveAndContinue: (Faculty?) -> Unit,
    ) : StudentProfileEditMessageState
    data class BatchSelection(
        val batches: List<Batch>,
        val selectedBatchId: Int?,
        val onSaveAndContinue: (Batch?) -> Unit,
    ) : StudentProfileEditMessageState
    data class ConfirmSave(val onConfirm: () -> Unit) : StudentProfileEditMessageState
    data class ShowSnackBar(val message: String) : StudentProfileEditMessageState
    data class Error(val message: String) : StudentProfileEditMessageState
}
