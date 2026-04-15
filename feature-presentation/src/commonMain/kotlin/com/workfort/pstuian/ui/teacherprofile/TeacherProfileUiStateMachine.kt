package com.workfort.pstuian.ui.teacherprofile

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.TeacherProfile
import com.workfort.pstuian.ui.teacherprofile.state.ProfileState
import com.workfort.pstuian.ui.teacherprofile.state.TeacherProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TeacherProfileUiStateMachine : UiStateMachine<TeacherProfileUiState> {

    private val _uiState = MutableStateFlow(TeacherProfileUiState())
    override val uiState: StateFlow<TeacherProfileUiState> = _uiState.asStateFlow()

    fun showProfileLoading() {
        _uiState.update { it.copy(profileState = ProfileState.Loading) }
    }

    fun showProfile(profile: TeacherProfile) {
        _uiState.update {
            it.copy(
                profileState = ProfileState.Available(profile),
                isSignedIn = profile.isSignedIn
            )
        }
    }

    fun showProfileError(message: String) {
        _uiState.update { it.copy(profileState = ProfileState.Error(message)) }
    }

    fun updateSelectedTab(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }

    fun updateSignedInState(isSignedIn: Boolean) {
        _uiState.update { it.copy(isSignedIn = isSignedIn) }
    }
}
