package com.workfort.pstuian.ui.profile.studentprofileedit.state

import com.workfort.pstuian.featuredomain.model.UserProfile

sealed interface StudentProfileEditUiEvent {
    data object LoadProfile : StudentProfileEditUiEvent
    data class ChangeProfile(val profile: UserProfile.StudentProfile) : StudentProfileEditUiEvent
    data object ClickBack : StudentProfileEditUiEvent
    data object ClickSave : StudentProfileEditUiEvent
    data object ClickFaculty : StudentProfileEditUiEvent
    data object ClickBatch : StudentProfileEditUiEvent
    data object Save : StudentProfileEditUiEvent
}
