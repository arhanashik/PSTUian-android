package com.workfort.pstuian.ui.profile.teacherprofile.state

import com.workfort.pstuian.featuredomain.model.UserType

sealed interface TeacherProfileNavigationState {
    data object GoBack : TeacherProfileNavigationState
    data object ResetToHome : TeacherProfileNavigationState
    data class ImagePreviewScreen(val encodedImageUrl: String) : TeacherProfileNavigationState
    data class ImageUploadScreen(val userId: Int) : TeacherProfileNavigationState
    data object ChangePasswordScreen : TeacherProfileNavigationState
    data class MyCheckInListScreen(val userId: Int) : TeacherProfileNavigationState
    data class TeacherProfileEditScreen(val userId: Int) : TeacherProfileNavigationState
    data object DeleteAccountScreen : TeacherProfileNavigationState
}