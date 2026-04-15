package com.workfort.pstuian.ui.employeeprofile.state

sealed interface EmployeeProfileUiEvent {
    data object LoadProfile : EmployeeProfileUiEvent
    data object BackClicked : EmployeeProfileUiEvent
    data class ImageClicked(val url: String) : EmployeeProfileUiEvent
    data object CallClicked : EmployeeProfileUiEvent
    data object EmailClicked : EmployeeProfileUiEvent
    data object SignOutClicked : EmployeeProfileUiEvent
    data class TabClicked(val index: Int) : EmployeeProfileUiEvent
    data object RefreshClicked : EmployeeProfileUiEvent
    data object ChangeImageClicked : EmployeeProfileUiEvent
    data object EditBioClicked : EmployeeProfileUiEvent
    data class EditClicked(val selectedTabIndex: Int) : EmployeeProfileUiEvent
    data object ChangePasswordClicked : EmployeeProfileUiEvent
    data object MyDeviceListClicked : EmployeeProfileUiEvent
    data object DeleteAccountClicked : EmployeeProfileUiEvent
    data class ChangeProfileImage(val imageUrl: String) : EmployeeProfileUiEvent
    data class ChangeBio(val newBio: String) : EmployeeProfileUiEvent
    data object SignOut : EmployeeProfileUiEvent
}
