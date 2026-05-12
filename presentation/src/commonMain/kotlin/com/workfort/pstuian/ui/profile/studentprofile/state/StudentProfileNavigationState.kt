package com.workfort.pstuian.ui.profile.studentprofile.state

sealed interface StudentProfileNavigationState {
    data object GoBack : StudentProfileNavigationState
    data object ResetToHome : StudentProfileNavigationState
    data class ImageUploadScreen(val userId: Int) : StudentProfileNavigationState
    data object ChangePasswordScreen : StudentProfileNavigationState
    data class BloodDonationHistoryScreen(val userId: Int) : StudentProfileNavigationState
    data class CheckInHistoryScreen(val userId: Int) : StudentProfileNavigationState
    data class StudentProfileEditScreen(val userId: Int) : StudentProfileNavigationState
    data object DeleteAccountScreen : StudentProfileNavigationState
    data class ImagePreviewScreen(val encodedImageUrl: String) : StudentProfileNavigationState
    data class OpenUrl(val url: String) : StudentProfileNavigationState
}