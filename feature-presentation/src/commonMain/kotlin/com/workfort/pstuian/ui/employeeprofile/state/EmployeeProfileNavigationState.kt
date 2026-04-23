package com.workfort.pstuian.ui.employeeprofile.state

import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.UserType

sealed interface EmployeeProfileNavigationState {
    data object GoBack : EmployeeProfileNavigationState
    data class ImagePreviewScreen(val encodedImageUrl: String) : EmployeeProfileNavigationState
    data class ImageUploadScreen(val userId: String, val userType: UserType) : EmployeeProfileNavigationState
    data object ChangePasswordScreen : EmployeeProfileNavigationState
    data class MyDeviceListScreen(val userId: String, val userType: UserType) : EmployeeProfileNavigationState
    data class EmployeeProfileEditScreen(val userId: String, val action: ProfileEditMode) : EmployeeProfileNavigationState
    data object DeleteAccountScreen : EmployeeProfileNavigationState
}