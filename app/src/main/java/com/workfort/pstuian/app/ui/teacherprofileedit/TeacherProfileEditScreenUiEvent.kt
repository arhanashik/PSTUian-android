package com.workfort.pstuian.app.ui.teacherprofileedit

import com.workfort.pstuian.model.TeacherProfile

sealed class TeacherProfileEditScreenUiEvent {
    data object None : TeacherProfileEditScreenUiEvent()
    data object OnLoadProfile : TeacherProfileEditScreenUiEvent()
    data class OnChangeProfile(val newProfile: TeacherProfile) : TeacherProfileEditScreenUiEvent()
    data object OnClickBack : TeacherProfileEditScreenUiEvent()
    data object OnClickSave : TeacherProfileEditScreenUiEvent()
    data object OnClickFaculty : TeacherProfileEditScreenUiEvent()
    data class OnChangeFaculty(val facultyId: Int) : TeacherProfileEditScreenUiEvent()
    data object OnSave : TeacherProfileEditScreenUiEvent()
    data object MessageConsumed : TeacherProfileEditScreenUiEvent()
    data object NavigationConsumed : TeacherProfileEditScreenUiEvent()
}