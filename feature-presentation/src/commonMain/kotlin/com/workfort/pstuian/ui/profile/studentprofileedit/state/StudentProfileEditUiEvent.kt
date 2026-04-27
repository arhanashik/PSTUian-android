package com.workfort.pstuian.ui.profile.studentprofileedit.state

import com.workfort.pstuian.featuredomain.model.UserProfile

sealed interface StudentProfileEditUiEvent {
    data object BackClicked : StudentProfileEditUiEvent
    data class TabClicked(val index: Int) : StudentProfileEditUiEvent
    data class ProfileInfoChanged(val profile: UserProfile.StudentProfile) : StudentProfileEditUiEvent
    data object FacultySelectionClicked : StudentProfileEditUiEvent
    data object BatchSelectionClicked : StudentProfileEditUiEvent
    data object AcademicInfoSaveClicked : StudentProfileEditUiEvent
    data object ConnectInfoSaveClicked : StudentProfileEditUiEvent
}
