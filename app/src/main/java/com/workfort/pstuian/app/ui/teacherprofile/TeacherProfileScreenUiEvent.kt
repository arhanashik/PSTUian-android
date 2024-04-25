package com.workfort.pstuian.app.ui.teacherprofile

import com.workfort.pstuian.model.ProfileInfoItemAction

sealed class TeacherProfileScreenUiEvent {
    data object None : TeacherProfileScreenUiEvent()
    data object OnLoadProfile : TeacherProfileScreenUiEvent()
    data object OnSignOut : TeacherProfileScreenUiEvent()
    data object OnClickBack : TeacherProfileScreenUiEvent()
    data object OnClickCall : TeacherProfileScreenUiEvent()
    data object OnClickSignOut : TeacherProfileScreenUiEvent()
    data class OnClickTab(val index: Int) : TeacherProfileScreenUiEvent()
    data object OnClickRefresh : TeacherProfileScreenUiEvent()
    data object OnClickChangeImage : TeacherProfileScreenUiEvent()
    data object OnClickEditBio : TeacherProfileScreenUiEvent()
    data class OnClickAction(val actionItem: ProfileInfoItemAction) : TeacherProfileScreenUiEvent()
    data class OnClickEdit(val selectedTabIndex: Int) : TeacherProfileScreenUiEvent()
    data class OnClickImage(val url: String) : TeacherProfileScreenUiEvent()
    data class OnEditBio(val newBio: String) : TeacherProfileScreenUiEvent()
    data class OnCall(val phoneNumber: String) : TeacherProfileScreenUiEvent()
    data class OnEmail(val email: String) : TeacherProfileScreenUiEvent()
    data object MessageConsumed : TeacherProfileScreenUiEvent()
    data object NavigationConsumed : TeacherProfileScreenUiEvent()
}