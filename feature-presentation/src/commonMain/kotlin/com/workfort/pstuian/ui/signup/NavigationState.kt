package com.workfort.pstuian.ui.signup

import com.workfort.pstuian.model.FacultySelectionMode

sealed interface NavigationState {
    data object GoBack : NavigationState
    data class GoToFacultyPickerScreen(
        val mode: FacultySelectionMode,
        val facultyId: Int?,
        val batchId: Int?,
    ) : NavigationState
}
