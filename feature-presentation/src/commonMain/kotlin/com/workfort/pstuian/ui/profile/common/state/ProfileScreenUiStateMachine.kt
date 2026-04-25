package com.workfort.pstuian.ui.profile.common.state

import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileHeaderDisplayData
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileScreenUiStateMachine : UiStateMachine<ProfileUiState> {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.None)
    override val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun showProfileLoading() {
        _uiState.update { ProfileUiState.Loading }
    }

    fun showProfile(
        headerDisplayData: ProfileHeaderDisplayData,
        academicContents: List<ProfileInfoItem>,
        connectContents: List<ProfileInfoItem>,
        isSignedIn: Boolean,
        selectedTabIndex: Int = 0,
    ) {
        _uiState.update {
            ProfileUiState.Content(
                headerDisplayData = headerDisplayData,
                academicContents = academicContents,
                connectContents = connectContents,
                isSignedIn = isSignedIn,
                selectedTabIndex = selectedTabIndex,
            )
        }
    }

    fun showProfileError(message: String) {
        _uiState.update { ProfileUiState.Error(message) }
    }

    fun updateSelectedTab(index: Int) {
        _uiState.update { current ->
            when (current) {
                is ProfileUiState.Content -> current.copy(selectedTabIndex = index)
                else -> current
            }
        }
    }

    fun updateSignedInState(isSignedIn: Boolean) {
        _uiState.update { current ->
            when (current) {
                is ProfileUiState.Content -> current.copy(isSignedIn = isSignedIn)
                else -> current
            }
        }
    }
}
