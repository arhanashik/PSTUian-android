package com.workfort.pstuian.app.ui.studentprofileedit

import com.workfort.pstuian.model.StudentProfile

sealed class StudentProfileEditScreenUiEvent {
    data object None : StudentProfileEditScreenUiEvent()
    data object OnLoadProfile : StudentProfileEditScreenUiEvent()
    data class OnChangeProfile(val profile: StudentProfile) : StudentProfileEditScreenUiEvent()
    data object OnClickBack : StudentProfileEditScreenUiEvent()
    data object OnClickSave : StudentProfileEditScreenUiEvent()
    data object OnClickFaculty : StudentProfileEditScreenUiEvent()
    data object OnClickBatch : StudentProfileEditScreenUiEvent()
    data class OnChangeFaculty(val facultyId: Int) : StudentProfileEditScreenUiEvent()
    data class OnChangeBatch(val batchId: Int) : StudentProfileEditScreenUiEvent()
    data object OnSave : StudentProfileEditScreenUiEvent()
    data object MessageConsumed : StudentProfileEditScreenUiEvent()
    data object NavigationConsumed : StudentProfileEditScreenUiEvent()
}