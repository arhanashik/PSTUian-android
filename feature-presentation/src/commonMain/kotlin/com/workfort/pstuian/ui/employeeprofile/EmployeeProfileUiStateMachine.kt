package com.workfort.pstuian.ui.employeeprofile

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.EmployeeProfile
import com.workfort.pstuian.ui.employeeprofile.state.EmployeeProfileUiState
import com.workfort.pstuian.ui.employeeprofile.state.ProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class EmployeeProfileUiStateMachine : UiStateMachine<EmployeeProfileUiState> {

    private val _uiState = MutableStateFlow(EmployeeProfileUiState())
    override val uiState: StateFlow<EmployeeProfileUiState> = _uiState.asStateFlow()

    fun showProfileLoading() {
        _uiState.update { it.copy(profileState = ProfileState.Loading) }
    }

    fun showProfile(profile: EmployeeProfile) {
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
