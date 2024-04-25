package com.workfort.pstuian.app.ui.studentprofile

import com.workfort.pstuian.model.ProfileInfoItemAction

sealed class StudentProfileScreenUiEvent {
    data object None : StudentProfileScreenUiEvent()
    data object OnLoadProfile : StudentProfileScreenUiEvent()
    data object OnSignOut : StudentProfileScreenUiEvent()
    data object OnClickBack : StudentProfileScreenUiEvent()
    data object OnClickCall : StudentProfileScreenUiEvent()
    data object OnClickSignOut : StudentProfileScreenUiEvent()
    data class OnClickTab(val index: Int) : StudentProfileScreenUiEvent()
    data object OnClickRefresh : StudentProfileScreenUiEvent()
    data object OnClickChangeImage : StudentProfileScreenUiEvent()
    data object OnClickEditBio : StudentProfileScreenUiEvent()
    data class OnClickAction(val actionItem: ProfileInfoItemAction) : StudentProfileScreenUiEvent()
    data class OnClickEdit(val selectedTabIndex: Int) : StudentProfileScreenUiEvent()
    data class OnClickImage(val url: String) : StudentProfileScreenUiEvent()
    data class OnEditBio(val newBio: String) : StudentProfileScreenUiEvent()
    data class OnCall(val phoneNumber: String) : StudentProfileScreenUiEvent()
    data class OnEmail(val email: String) : StudentProfileScreenUiEvent()
    data object MessageConsumed : StudentProfileScreenUiEvent()
    data object NavigationConsumed : StudentProfileScreenUiEvent()
}