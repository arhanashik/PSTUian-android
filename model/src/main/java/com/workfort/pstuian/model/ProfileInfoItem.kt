package com.workfort.pstuian.model

data class ProfileInfoItem (
    val label: String,
    val title: String,
    val action: ProfileInfoItemAction = ProfileInfoItemAction.None,
)

sealed class ProfileInfoItemAction {
    data object None : ProfileInfoItemAction()
    data object Edit : ProfileInfoItemAction()
    data class Call(val phoneNumber: String) : ProfileInfoItemAction()
    data class Email(val email: String) : ProfileInfoItemAction()
    data class DownloadCv(val url: String) : ProfileInfoItemAction()
    data class Link(val url: String) : ProfileInfoItemAction()
    data object Password : ProfileInfoItemAction()
    data object UploadCv : ProfileInfoItemAction()
    data object BloodDonationList : ProfileInfoItemAction()
    data object CheckInList : ProfileInfoItemAction()
    data object SignedInDevices : ProfileInfoItemAction()
    data object DeleteAccount : ProfileInfoItemAction()
}