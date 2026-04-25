package com.workfort.pstuian.ui.profile.studentprofile.state

import com.workfort.pstuian.featuredomain.model.StudentProfile

data class StudentProfileUiState(
    val selectedTabIndex: Int = 0,
    val isSignedIn: Boolean = false,
    val profileState: ProfileState = ProfileState.None,
)

sealed interface ProfileState {
    data object None : ProfileState
    data object Loading : ProfileState
    data class Available(val profile: StudentProfile) : ProfileState
    data class Error(val message: String) : ProfileState
}
