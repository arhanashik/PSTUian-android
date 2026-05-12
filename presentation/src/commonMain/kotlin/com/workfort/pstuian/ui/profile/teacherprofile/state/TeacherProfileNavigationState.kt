package com.workfort.pstuian.ui.profile.teacherprofile.state

sealed interface TeacherProfileNavigationState {
    data object GoBack : TeacherProfileNavigationState
    data object ResetToHome : TeacherProfileNavigationState
    data class ImagePreviewScreen(val encodedImageUrl: String) : TeacherProfileNavigationState
    data class ImageUploadScreen(val userId: Int) : TeacherProfileNavigationState
    data object ChangePasswordScreen : TeacherProfileNavigationState
    data class CheckInHistoryScreen(val userId: Int) : TeacherProfileNavigationState
    data class TeacherProfileEditScreen(val userId: Int) : TeacherProfileNavigationState
    data object DeleteAccountScreen : TeacherProfileNavigationState
    data class OpenUrl(val url: String) : TeacherProfileNavigationState
}