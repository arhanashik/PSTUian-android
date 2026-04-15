package com.workfort.pstuian.ui.studentprofile.state

import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.UserType

sealed interface StudentProfileNavigationState {
    data object GoBack : StudentProfileNavigationState
    data class ImageUploadScreen(
        val userId: Int,
        val userType: UserType,
    ) : StudentProfileNavigationState
    data object ChangePasswordScreen : StudentProfileNavigationState
    data class DownloadCvScreen(
        val userId: Int,
        val userType: UserType,
        val url: String,
    ) : StudentProfileNavigationState
    data class UploadCvScreen(
        val userId: Int,
        val userType: UserType,
    ) : StudentProfileNavigationState
    data class MyBloodDonationListScreen(
        val userId: Int,
        val userType: UserType,
    ) : StudentProfileNavigationState
    data class MyCheckInListScreen(
        val userId: Int,
        val userType: UserType,
    ) : StudentProfileNavigationState
    data class MyDeviceListScreen(
        val userId: Int,
        val userType: UserType,
    ) : StudentProfileNavigationState
    data class StudentProfileEditScreen(
        val userId: Int,
        val action: ProfileEditMode,
    ) : StudentProfileNavigationState
    data class DeleteAccountScreen(
        val userId: Int,
        val userType: UserType,
    ) : StudentProfileNavigationState
    data class ImagePreviewScreen(val encodedImageUrl: String) : StudentProfileNavigationState
}