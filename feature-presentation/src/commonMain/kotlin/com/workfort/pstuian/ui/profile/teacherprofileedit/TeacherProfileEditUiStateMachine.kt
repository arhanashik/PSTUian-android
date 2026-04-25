package com.workfort.pstuian.ui.profile.teacherprofileedit

import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.TeacherAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.TeacherConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.TeacherProfile
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.profile.teacherprofileedit.state.TeacherProfileEditUiState
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class TeacherProfileEditUiStateMachine : UiStateMachine<TeacherProfileEditUiState> {
    private val _uiState = MutableStateFlow<TeacherProfileEditUiState>(TeacherProfileEditUiState.None)
    override val uiState: StateFlow<TeacherProfileEditUiState> = _uiState

    fun setInitialContent() {
        _uiState.update { TeacherProfileEditUiState.Content() }
    }

    fun updateProfileScreenState(
        profile: TeacherProfile,
        academicValidationError: TeacherAcademicInfoInputError,
        connectValidationError: TeacherConnectInfoInputError,
        mode: ProfileEditMode,
    ) {
        val panelState = when (mode) {
            ProfileEditMode.ACADEMIC -> {
                TeacherProfileEditUiState.PanelState.Academic(
                    profile, academicValidationError
                )
            }
            ProfileEditMode.CONNECT -> {
                TeacherProfileEditUiState.PanelState.Connect(
                    profile, connectValidationError
                )
            }
        }
        updatePanelState(panelState)
    }

    fun updatePanelState(panelState: TeacherProfileEditUiState.PanelState) {
        _uiState.update {
            if (it is TeacherProfileEditUiState.Content) {
                it.copy(panelState = panelState)
            } else {
                TeacherProfileEditUiState.Content(panelState = panelState)
            }
        }
    }

    fun validateAcademic(profile: TeacherProfile) = TeacherAcademicInfoInputError.INITIAL.copy(
        name = if (profile.teacher.name.isEmpty()) "*Required" else "",
        designation = if (profile.teacher.designation.isEmpty()) "*Required" else "",
        department = if (profile.teacher.department.isEmpty()) "*Required" else "",
    )

    fun validateConnect(profile: TeacherProfile) = TeacherConnectInfoInputError.INITIAL.copy(
        email = if (profile.teacher.email.isNullOrEmpty()) {
            "*Required"
        } else if (profile.teacher.email?.isValidEmail() == false) {
            "*Invalid email"
        } else {
            ""
        },
    )
}
