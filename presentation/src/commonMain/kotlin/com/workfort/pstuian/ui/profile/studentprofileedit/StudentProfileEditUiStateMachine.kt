package com.workfort.pstuian.ui.profile.studentprofileedit

import com.workfort.pstuian.featuredomain.model.StudentAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.StudentConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class StudentProfileEditUiStateMachine : UiStateMachine<StudentProfileEditUiState> {
    private val _uiState = MutableStateFlow<StudentProfileEditUiState>(StudentProfileEditUiState.None)
    override val uiState: StateFlow<StudentProfileEditUiState> = _uiState

    fun updateProfileContent(profile: UserProfile.StudentProfile) {
        _uiState.update { current ->
            when (current) {
                is StudentProfileEditUiState.Content -> current.copy(profile = profile, isLoading = false)
                else -> StudentProfileEditUiState.Content(profile = profile, isLoading = false)
            }
        }
    }

    fun updateTabIndex(index: Int) {
        _uiState.update { current ->
            when (current) {
                is StudentProfileEditUiState.Content -> current.copy(selectedTabIndex = index)
                else -> current
            }
        }
    }

    fun updateAcademicInfoInputError(academicInfoInputError: StudentAcademicInfoInputError) {
        _uiState.update { current ->
            when (current) {
                is StudentProfileEditUiState.Content -> current.copy(
                    academicInfoInputError = academicInfoInputError,
                    isLoading = false,
                )
                 else -> current
            }
        }
    }

    fun updateConnectInfoInputError(connectInfoInputError: StudentConnectInfoInputError) {
        _uiState.update { current ->
            when (current) {
                is StudentProfileEditUiState.Content -> current.copy(
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
                is StudentProfileEditUiState.Content -> current.copy(isLoading = isLoading)
                else -> current
            }
        }
    }
}
