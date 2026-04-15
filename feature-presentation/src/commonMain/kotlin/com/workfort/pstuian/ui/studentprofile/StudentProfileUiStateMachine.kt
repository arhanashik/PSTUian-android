package com.workfort.pstuian.ui.studentprofile

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.StudentProfile
import com.workfort.pstuian.ui.studentprofile.state.ProfileState
import com.workfort.pstuian.ui.studentprofile.state.StudentProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StudentProfileUiStateMachine : UiStateMachine<StudentProfileUiState> {

    private val _uiState = MutableStateFlow(StudentProfileUiState())
    override val uiState: StateFlow<StudentProfileUiState> = _uiState.asStateFlow()

    fun showProfileLoading() {
        _uiState.update { it.copy(profileState = ProfileState.Loading) }
    }

    fun showProfile(profile: StudentProfile) {
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
