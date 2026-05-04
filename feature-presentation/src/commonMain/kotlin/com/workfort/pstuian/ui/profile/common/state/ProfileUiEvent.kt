package com.workfort.pstuian.ui.profile.common.state

sealed interface ProfileUiEvent {
    data object BackClicked : ProfileUiEvent
    data object FollowClicked : ProfileUiEvent
    data class ImageClicked(val url: String) : ProfileUiEvent
    data object CallClicked : ProfileUiEvent
    data object EmailClicked : ProfileUiEvent
    data object SignOutClicked : ProfileUiEvent
    data class TabClicked(val index: Int) : ProfileUiEvent
    data object RefreshClicked : ProfileUiEvent
    data object ChangeImageClicked : ProfileUiEvent
    data object EditBioClicked : ProfileUiEvent
    data object EditClicked : ProfileUiEvent
    data object MyBloodDonationListClicked : ProfileUiEvent
    data object ChangePasswordClicked : ProfileUiEvent
    data class DownloadCvClicked(val url: String) : ProfileUiEvent
    data object UploadCvClicked : ProfileUiEvent
    data object MyCheckInListClicked : ProfileUiEvent
    data object MyDeviceListClicked : ProfileUiEvent
    data object DeleteAccountClicked : ProfileUiEvent
}