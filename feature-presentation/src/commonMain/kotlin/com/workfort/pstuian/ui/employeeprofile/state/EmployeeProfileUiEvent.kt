package com.workfort.pstuian.ui.employeeprofile.state

sealed interface EmployeeProfileUiEvent {
    data object LoadProfile : EmployeeProfileUiEvent
    data object ClickBack : EmployeeProfileUiEvent
    data class ClickImage(val url: String) : EmployeeProfileUiEvent
    data object ClickCall : EmployeeProfileUiEvent
    data object ClickEmail : EmployeeProfileUiEvent
    data object ClickSignOut : EmployeeProfileUiEvent
    data class ClickTab(val index: Int) : EmployeeProfileUiEvent
    data object ClickRefresh : EmployeeProfileUiEvent
    data object ClickChangeImage : EmployeeProfileUiEvent
    data object ClickEditBio : EmployeeProfileUiEvent
    data class ClickEdit(val selectedTabIndex: Int) : EmployeeProfileUiEvent
    data object ClickChangePassword : EmployeeProfileUiEvent
    data object ClickMyDeviceList : EmployeeProfileUiEvent
    data object ClickDeleteAccount : EmployeeProfileUiEvent
    data class ChangeProfileImage(val imageUrl: String) : EmployeeProfileUiEvent
    data class ChangeBio(val newBio: String) : EmployeeProfileUiEvent
    data object SignOut : EmployeeProfileUiEvent
    data class OnCall(val phoneNumber: String) : EmployeeProfileUiEvent
    data class OnEmail(val email: String) : EmployeeProfileUiEvent
    data object MessageConsumed : EmployeeProfileUiEvent
    data object NavigationConsumed : EmployeeProfileUiEvent
}
