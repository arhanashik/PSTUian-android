package com.workfort.pstuian.app.ui.employeeprofile

import com.workfort.pstuian.model.ProfileInfoItemAction

sealed class EmployeeProfileScreenUiEvent {
    data object None : EmployeeProfileScreenUiEvent()
    data object OnLoadProfile : EmployeeProfileScreenUiEvent()
    data object OnClickBack : EmployeeProfileScreenUiEvent()
    data object OnClickCall : EmployeeProfileScreenUiEvent()
    data class OnClickTab(val index: Int) : EmployeeProfileScreenUiEvent()
    data class OnClickImage(val url: String) : EmployeeProfileScreenUiEvent()
    data object OnClickBio : EmployeeProfileScreenUiEvent()
    data class OnClickAction(val actionItem: ProfileInfoItemAction) : EmployeeProfileScreenUiEvent()
    data class OnCall(val phoneNumber: String) : EmployeeProfileScreenUiEvent()
    data object MessageConsumed : EmployeeProfileScreenUiEvent()
    data object NavigationConsumed : EmployeeProfileScreenUiEvent()
}