package com.workfort.pstuian.ui.studentprofileedit.state

import com.workfort.pstuian.featuredomain.model.StudentProfile

sealed interface StudentProfileEditUiEvent {
    data object LoadProfile : StudentProfileEditUiEvent
    data class ChangeProfile(val profile: StudentProfile) : StudentProfileEditUiEvent
    data object ClickBack : StudentProfileEditUiEvent
    data object ClickSave : StudentProfileEditUiEvent
    data object ClickFaculty : StudentProfileEditUiEvent
    data object ClickBatch : StudentProfileEditUiEvent
    data class ChangeFaculty(val facultyId: Int) : StudentProfileEditUiEvent
    data class ChangeBatch(val batchId: Int) : StudentProfileEditUiEvent
    data object Save : StudentProfileEditUiEvent
    data object MessageConsumed : StudentProfileEditUiEvent
    data object NavigationConsumed : StudentProfileEditUiEvent
}
