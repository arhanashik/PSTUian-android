package com.workfort.pstuian.ui.profile.studentprofileedit.state

import com.workfort.pstuian.featuredomain.model.FacultySelectionMode

sealed interface StudentProfileEditNavigationState {
    data object GoBack : StudentProfileEditNavigationState
    data class GoToFacultyPickerScreen(
        val mode: FacultySelectionMode,
        val facultyId: Int,
        val batchId: Int,
    ) : StudentProfileEditNavigationState
}
