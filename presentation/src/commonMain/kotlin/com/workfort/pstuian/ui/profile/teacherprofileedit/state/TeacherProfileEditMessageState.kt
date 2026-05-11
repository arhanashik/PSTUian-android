package com.workfort.pstuian.ui.profile.teacherprofileedit.state

import com.workfort.pstuian.featuredomain.model.Faculty

sealed interface TeacherProfileEditMessageState {
    data class Loading(val cancelable: Boolean = false) : TeacherProfileEditMessageState
    data class FacultySelection(
        val faculties: List<Faculty>,
        val selectedFacultyId: Int?,
        val onSaveAndContinue: (Faculty?) -> Unit,
    ) : TeacherProfileEditMessageState
    data class ConfirmSave(val onConfirm: () -> Unit) : TeacherProfileEditMessageState
    data class ShowSnackBar(val message: String) : TeacherProfileEditMessageState
    data class Error(val message: String) : TeacherProfileEditMessageState
}
