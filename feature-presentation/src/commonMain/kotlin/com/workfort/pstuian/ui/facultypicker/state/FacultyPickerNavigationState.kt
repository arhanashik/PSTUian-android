package com.workfort.pstuian.ui.facultypicker.state

sealed interface FacultyPickerNavigationState {
    data class GoBack(
        val selectedFacultyId: Int?,
        val selectedBatchId: Int?,
    ) : FacultyPickerNavigationState
}
