package com.workfort.pstuian.ui.profile.teacherprofile.state

import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.UserType

sealed interface TeacherProfileNavigationState {
    data object GoBack : TeacherProfileNavigationState
    data class ImagePreviewScreen(val encodedImageUrl: String) : TeacherProfileNavigationState
    data class ImageUploadScreen(val userId: String, val userType: UserType) : TeacherProfileNavigationState
    data object ChangePasswordScreen : TeacherProfileNavigationState
    data class MyDeviceListScreen(val userId: String, val userType: UserType) : TeacherProfileNavigationState
    data class TeacherProfileEditScreen(val userId: String, val action: ProfileEditMode) : TeacherProfileNavigationState
    data object DeleteAccountScreen : TeacherProfileNavigationState
}