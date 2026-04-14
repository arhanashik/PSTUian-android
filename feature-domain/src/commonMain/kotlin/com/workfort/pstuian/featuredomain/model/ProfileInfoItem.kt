package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileInfoItem (
    val label: String,
    val title: String,
    val action: ProfileInfoItemAction = ProfileInfoItemAction.None,
)

@Serializable
sealed class ProfileInfoItemAction {
    @Serializable
    data object None : ProfileInfoItemAction()
    @Serializable
    data object Edit : ProfileInfoItemAction()
    @Serializable
    data class Call(val phoneNumber: String) : ProfileInfoItemAction()
    @Serializable
    data class Email(val email: String) : ProfileInfoItemAction()
    @Serializable
    data class DownloadCv(val url: String) : ProfileInfoItemAction()
    @Serializable
    data class Link(val url: String) : ProfileInfoItemAction()
    @Serializable
    data object Password : ProfileInfoItemAction()
    @Serializable
    data object UploadCv : ProfileInfoItemAction()
    @Serializable
    data object BloodDonationList : ProfileInfoItemAction()
    @Serializable
    data object CheckInList : ProfileInfoItemAction()
    @Serializable
    data object SignedInDevices : ProfileInfoItemAction()
    @Serializable
    data object DeleteAccount : ProfileInfoItemAction()
}
