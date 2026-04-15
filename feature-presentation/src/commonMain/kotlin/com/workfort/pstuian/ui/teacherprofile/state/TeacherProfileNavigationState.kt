package com.workfort.pstuian.ui.teacherprofile.state

import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.UserType

sealed interface TeacherProfileNavigationState {
    data object GoBack : TeacherProfileNavigationState
    data class ImagePreviewScreen(val encodedImageUrl: String) : TeacherProfileNavigationState
    data class ImageUploadScreen(val userId: Int, val userType: UserType) : TeacherProfileNavigationState
    data object ChangePasswordScreen : TeacherProfileNavigationState
    data class MyDeviceListScreen(val userId: Int, val userType: UserType) : TeacherProfileNavigationState
    data class TeacherProfileEditScreen(val userId: Int, val action: ProfileEditMode) : TeacherProfileNavigationState
    data class DeleteAccountScreen(val userId: Int, val userType: UserType) : TeacherProfileNavigationState
}