package com.workfort.pstuian.ui.employeeprofile.state

import com.workfort.pstuian.featuredomain.model.EmployeeProfile
import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.UserType

data class EmployeeProfileUiState(
    val profileState: ProfileState = ProfileState.None,
    val selectedTabIndex: Int = 0,
    val isSignedIn: Boolean = false,
)

sealed interface ProfileState {
    data object None : ProfileState
    data object Loading : ProfileState
    data class Available(val profile: EmployeeProfile) : ProfileState
    data class Error(val message: String) : ProfileState
}
