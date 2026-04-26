package com.workfort.pstuian.ui.profile.teacherprofileedit.state

import com.workfort.pstuian.featuredomain.model.UserProfile

sealed interface TeacherProfileEditUiEvent {
    data object LoadProfile : TeacherProfileEditUiEvent
    data class ChangeProfile(val profile: UserProfile.TeacherProfile) : TeacherProfileEditUiEvent
    data object ClickBack : TeacherProfileEditUiEvent
    data object ClickSave : TeacherProfileEditUiEvent
    data object ClickFaculty : TeacherProfileEditUiEvent
    data class ChangeFaculty(val facultyId: Int) : TeacherProfileEditUiEvent
    data object MessageConsumed : TeacherProfileEditUiEvent
    data object NavigationConsumed : TeacherProfileEditUiEvent
}
