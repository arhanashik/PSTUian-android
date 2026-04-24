package com.workfort.pstuian.ui.studentprofileedit.state

import com.workfort.pstuian.featuredomain.model.StudentProfile

sealed interface StudentProfileEditUiEvent {
    data object LoadProfile : StudentProfileEditUiEvent
    data class ChangeProfile(val profile: StudentProfile) : StudentProfileEditUiEvent
    data object ClickBack : StudentProfileEditUiEvent
    data object ClickSave : StudentProfileEditUiEvent
    data object ClickFaculty : StudentProfileEditUiEvent
    data object ClickBatch : StudentProfileEditUiEvent
    data object Save : StudentProfileEditUiEvent
}
