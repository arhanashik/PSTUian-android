package com.workfort.pstuian.ui.studentprofile.state

sealed interface StudentProfileUiEvent {
    data object BackClicked : StudentProfileUiEvent
    data object FollowClicked : StudentProfileUiEvent
    data class ImageClicked(val url: String) : StudentProfileUiEvent
    data object CallClicked : StudentProfileUiEvent
    data object EmailClicked : StudentProfileUiEvent
    data object SignOutClicked : StudentProfileUiEvent
    data class TabClicked(val index: Int) : StudentProfileUiEvent
    data object RefreshClicked : StudentProfileUiEvent
    data object ChangeImageClicked : StudentProfileUiEvent
    data object EditBioClicked : StudentProfileUiEvent
    data class EditClicked(val selectedTabIndex: Int) : StudentProfileUiEvent
    data object MyBloodDonationListClicked : StudentProfileUiEvent
    data object ChangePasswordClicked : StudentProfileUiEvent
    data class DownloadCvClicked(val url: String) : StudentProfileUiEvent
    data object UploadCvClicked : StudentProfileUiEvent
    data object MyCheckInListClicked : StudentProfileUiEvent
    data object MyDeviceListClicked : StudentProfileUiEvent
    data object DeleteAccountClicked : StudentProfileUiEvent
    data class ChangeProfileImage(val imageUrl: String) : StudentProfileUiEvent
}
