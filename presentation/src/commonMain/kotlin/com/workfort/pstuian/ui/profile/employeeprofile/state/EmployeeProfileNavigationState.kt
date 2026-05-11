package com.workfort.pstuian.ui.profile.employeeprofile.state

sealed interface EmployeeProfileNavigationState {
    data object GoBack : EmployeeProfileNavigationState
    data object ResetToHome : EmployeeProfileNavigationState
    data class ImagePreviewScreen(val encodedImageUrl: String) : EmployeeProfileNavigationState
    data class ImageUploadScreen(val userId: Int) : EmployeeProfileNavigationState
    data object ChangePasswordScreen : EmployeeProfileNavigationState
    data class EmployeeProfileEditScreen(val userId: Int) : EmployeeProfileNavigationState
    data object DeleteAccountScreen : EmployeeProfileNavigationState
}