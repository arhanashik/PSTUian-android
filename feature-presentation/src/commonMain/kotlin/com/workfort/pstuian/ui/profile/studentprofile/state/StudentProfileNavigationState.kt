package com.workfort.pstuian.ui.profile.studentprofile.state

import com.workfort.pstuian.featuredomain.model.UserType

sealed interface StudentProfileNavigationState {
    data object GoBack : StudentProfileNavigationState
    data object ResetToHome : StudentProfileNavigationState
    data class ImageUploadScreen(val userId: Int) : StudentProfileNavigationState
    data object ChangePasswordScreen : StudentProfileNavigationState
    data class DownloadCvScreen(val userId: Int, val url: String) : StudentProfileNavigationState
    data class UploadCvScreen(val userId: Int) : StudentProfileNavigationState
    data class MyBloodDonationListScreen(val userId: Int) : StudentProfileNavigationState
    data class MyCheckInListScreen(val userId: Int) : StudentProfileNavigationState
    data class MyDeviceListScreen(val userId: Int) : StudentProfileNavigationState
    data class StudentProfileEditScreen(val userId: Int) : StudentProfileNavigationState
    data object DeleteAccountScreen : StudentProfileNavigationState
    data class ImagePreviewScreen(val encodedImageUrl: String) : StudentProfileNavigationState
}