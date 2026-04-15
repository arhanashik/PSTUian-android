package com.workfort.pstuian.ui.studentprofileedit

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.StudentAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.StudentConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.StudentProfile
import com.workfort.pstuian.ui.studentprofileedit.state.StudentProfileEditUiState
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class StudentProfileEditUiStateMachine : UiStateMachine<StudentProfileEditUiState> {
    private val _uiState = MutableStateFlow<StudentProfileEditUiState>(StudentProfileEditUiState.None)
    override val uiState: StateFlow<StudentProfileEditUiState> = _uiState

    fun setInitialContent() {
        _uiState.update { StudentProfileEditUiState.Content() }
    }

    fun updateProfileScreenState(
        profile: StudentProfile,
        academicValidationError: StudentAcademicInfoInputError,
        connectValidationError: StudentConnectInfoInputError,
        mode: ProfileEditMode,
    ) {
        val panelState = when (mode) {
            ProfileEditMode.ACADEMIC -> {
                StudentProfileEditUiState.PanelState.Academic(
                    profile, academicValidationError
                )
            }
            ProfileEditMode.CONNECT -> {
                StudentProfileEditUiState.PanelState.Connect(
                    profile, connectValidationError
                )
            }
        }
        updatePanelState(panelState)
    }

    fun updatePanelState(panelState: StudentProfileEditUiState.PanelState) {
        _uiState.update {
            if (it is StudentProfileEditUiState.Content) {
                it.copy(panelState = panelState)
            } else {
                StudentProfileEditUiState.Content(panelState = panelState)
            }
        }
    }

    fun validateAcademic(profile: StudentProfile) = StudentAcademicInfoInputError.INITIAL.copy(
        name = if (profile.student.name.isEmpty()) "*Required" else "",
        id = if (profile.student.id == 0) "*Required" else "",
        reg = if (profile.student.reg.isEmpty()) "*Required" else "",
        session = if (profile.student.session.isEmpty()) "*Required" else "",
    )

    fun validateConnect(profile: StudentProfile) = StudentConnectInfoInputError.INITIAL.copy(
        email = if (profile.student.email.isNullOrEmpty()) {
            "*Required"
        } else if (profile.student.email?.isValidEmail() == false) {
            "*Invalid email"
        } else {
            ""
        },
    )
}
