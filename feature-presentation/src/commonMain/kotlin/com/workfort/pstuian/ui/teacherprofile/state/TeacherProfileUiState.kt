package com.workfort.pstuian.ui.teacherprofile.state

import com.workfort.pstuian.featuredomain.model.TeacherProfile

data class TeacherProfileUiState(
    val profileState: ProfileState = ProfileState.None,
    val selectedTabIndex: Int = 0,
    val isSignedIn: Boolean = false,
)

sealed interface ProfileState {
    data object None : ProfileState
    data object Loading : ProfileState
    data class Available(val profile: TeacherProfile) : ProfileState
    data class Error(val message: String) : ProfileState
}