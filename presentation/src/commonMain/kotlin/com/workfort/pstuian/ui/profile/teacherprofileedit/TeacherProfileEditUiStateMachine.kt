package com.workfort.pstuian.ui.profile.teacherprofileedit

import com.workfort.pstuian.featuredomain.model.TeacherAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.TeacherConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.profile.teacherprofileedit.state.TeacherProfileEditUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class TeacherProfileEditUiStateMachine : UiStateMachine<TeacherProfileEditUiState> {
    private val _uiState = MutableStateFlow<TeacherProfileEditUiState>(TeacherProfileEditUiState.None)
    override val uiState: StateFlow<TeacherProfileEditUiState> = _uiState

    fun updateProfileContent(profile: UserProfile.TeacherProfile) {
        _uiState.update { current ->
            when (current) {
                is TeacherProfileEditUiState.Content -> current.copy(profile = profile, isLoading = false)
                else -> TeacherProfileEditUiState.Content(profile = profile, isLoading = false)
            }
        }
    }

    fun updateTabIndex(index: Int) {
        _uiState.update { current ->
            when (current) {
                is TeacherProfileEditUiState.Content -> current.copy(selectedTabIndex = index)
                else -> current
            }
        }
    }

    fun updateAcademicInfoInputError(academicInfoInputError: TeacherAcademicInfoInputError) {
        _uiState.update { current ->
            when (current) {
                is TeacherProfileEditUiState.Content -> current.copy(
                    academicInfoInputError = academicInfoInputError,
                    isLoading = false,
                )
                else -> current
            }
        }
    }

    fun updateConnectInfoInputError(connectInfoInputError: TeacherConnectInfoInputError) {
        _uiState.update { current ->
            when (current) {
                is TeacherProfileEditUiState.Content -> current.copy(
                    connectInfoInputError = connectInfoInputError,
                    isLoading = false,
                )
                else -> current
            }
        }
    }

    fun showLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is TeacherProfileEditUiState.Content -> current.copy(isLoading = isLoading)
                else -> current
            }
        }
    }
}
