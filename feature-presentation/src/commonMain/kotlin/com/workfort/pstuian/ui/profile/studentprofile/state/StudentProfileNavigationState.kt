package com.workfort.pstuian.ui.profile.studentprofile.state

import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.UserType

sealed interface StudentProfileNavigationState {
    data object GoBack : StudentProfileNavigationState
    data class ImageUploadScreen(
        val userId: String,
        val userType: UserType,
    ) : StudentProfileNavigationState
    data object ChangePasswordScreen : StudentProfileNavigationState
    data class DownloadCvScreen(
        val userId: String,
        val userType: UserType,
        val url: String,
    ) : StudentProfileNavigationState
    data class UploadCvScreen(
        val userId: String,
        val userType: UserType,
    ) : StudentProfileNavigationState
    data class MyBloodDonationListScreen(
        val userId: String,
        val userType: UserType,
    ) : StudentProfileNavigationState
    data class MyCheckInListScreen(
        val userId: String,
        val userType: UserType,
    ) : StudentProfileNavigationState
    data class MyDeviceListScreen(
        val userId: String,
        val userType: UserType,
    ) : StudentProfileNavigationState
    data class StudentProfileEditScreen(
        val userId: Int,
        val action: ProfileEditMode,
    ) : StudentProfileNavigationState
    data object DeleteAccountScreen : StudentProfileNavigationState
    data class ImagePreviewScreen(val encodedImageUrl: String) : StudentProfileNavigationState
}