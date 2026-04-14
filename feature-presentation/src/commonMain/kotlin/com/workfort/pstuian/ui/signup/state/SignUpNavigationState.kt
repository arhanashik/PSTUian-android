package com.workfort.pstuian.ui.signup.state

import com.workfort.pstuian.featuredomain.model.FacultySelectionMode

sealed interface SignUpNavigationState {
    data object GoBack : SignUpNavigationState
    data class GoToFacultyPickerScreen(
        val mode: FacultySelectionMode,
        val facultyId: Int?,
        val batchId: Int?,
    ) : SignUpNavigationState
}
