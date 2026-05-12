package com.workfort.pstuian.ui.profile.employeeprofile.state

import com.workfort.pstuian.ui.profile.studentprofile.state.StudentProfileNavigationState

sealed interface EmployeeProfileNavigationState {
    data object GoBack : EmployeeProfileNavigationState
    data object ResetToHome : EmployeeProfileNavigationState
    data class ImagePreviewScreen(val encodedImageUrl: String) : EmployeeProfileNavigationState
    data class ImageUploadScreen(val userId: Int) : EmployeeProfileNavigationState
    data object ChangePasswordScreen : EmployeeProfileNavigationState
    data class EmployeeProfileEditScreen(val userId: Int) : EmployeeProfileNavigationState
    data object DeleteAccountScreen : EmployeeProfileNavigationState
    data class OpenUrl(val url: String) : EmployeeProfileNavigationState
}