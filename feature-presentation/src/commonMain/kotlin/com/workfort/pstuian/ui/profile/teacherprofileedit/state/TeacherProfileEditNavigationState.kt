package com.workfort.pstuian.ui.profile.teacherprofileedit.state

import com.workfort.pstuian.featuredomain.model.FacultySelectionMode

sealed interface TeacherProfileEditNavigationState {
    data object GoBack : TeacherProfileEditNavigationState
    data class GoToFacultyPickerScreen(
        val mode: FacultySelectionMode,
        val facultyId: Int,
    ) : TeacherProfileEditNavigationState
}
