package com.workfort.pstuian.ui.studentprofile.state

sealed interface StudentProfileUiEvent {
    data object LoadProfile : StudentProfileUiEvent
    data object ClickBack : StudentProfileUiEvent
    data class ClickImage(val url: String) : StudentProfileUiEvent
    data object ClickCall : StudentProfileUiEvent
    data object ClickEmail : StudentProfileUiEvent
    data object ClickSignOut : StudentProfileUiEvent
    data class ClickTab(val index: Int) : StudentProfileUiEvent
    data object ClickRefresh : StudentProfileUiEvent
    data object ClickChangeImage : StudentProfileUiEvent
    data object ClickEditBio : StudentProfileUiEvent
    data class ClickEdit(val selectedTabIndex: Int) : StudentProfileUiEvent
    data object ClickMyBloodDonationList : StudentProfileUiEvent
    data object ClickChangePassword : StudentProfileUiEvent
    data class ClickDownloadCv(val url: String) : StudentProfileUiEvent
    data object ClickUploadCv : StudentProfileUiEvent
    data object ClickMyCheckInList : StudentProfileUiEvent
    data object ClickMyDeviceList : StudentProfileUiEvent
    data object ClickDeleteAccount : StudentProfileUiEvent
    data class ChangeProfileImage(val imageUrl: String) : StudentProfileUiEvent
    data class ChangeBio(val newBio: String) : StudentProfileUiEvent
    data class OnCall(val phoneNumber: String) : StudentProfileUiEvent
    data class OnEmail(val email: String) : StudentProfileUiEvent
    data object SignOut : StudentProfileUiEvent
    data object MessageConsumed : StudentProfileUiEvent
    data object NavigationConsumed : StudentProfileUiEvent
}
