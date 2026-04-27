package com.workfort.pstuian.ui.profile.teacherprofileedit.state

import com.workfort.pstuian.featuredomain.model.UserProfile

sealed interface TeacherProfileEditUiEvent {
    data object BackClicked : TeacherProfileEditUiEvent
    data class TabClicked(val index: Int) : TeacherProfileEditUiEvent
    data class ProfileInfoChanged(val profile: UserProfile.TeacherProfile) : TeacherProfileEditUiEvent
    data object FacultySelectionClicked : TeacherProfileEditUiEvent
    data object AcademicInfoSaveClicked : TeacherProfileEditUiEvent
    data object ConnectInfoSaveClicked : TeacherProfileEditUiEvent
}
