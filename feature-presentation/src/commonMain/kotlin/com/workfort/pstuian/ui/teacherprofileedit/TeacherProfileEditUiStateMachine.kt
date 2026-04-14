package com.workfort.pstuian.ui.teacherprofileedit

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.FacultySelectionMode
import com.workfort.pstuian.featuredomain.model.TeacherAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.TeacherConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.TeacherProfile
import com.workfort.pstuian.ui.teacherprofileedit.state.TeacherProfileEditUiState
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class TeacherProfileEditUiStateMachine : UiStateMachine<TeacherProfileEditUiState> {
    private val _uiState = MutableStateFlow(TeacherProfileEditUiState())
    override val uiState: StateFlow<TeacherProfileEditUiState> = _uiState

    fun messageConsumed() {
        _uiState.update { it.copy(displayState = it.displayState.copy(messageState = null)) }
    }

    fun navigationConsumed() {
        _uiState.update { it.copy(navigationState = null) }
    }

    fun onClickBack() {
        _uiState.update {
            it.copy(navigationState = TeacherProfileEditUiState.NavigationState.GoBack)
        }
    }

    fun onClickSave() {
        updateMessageState(TeacherProfileEditUiState.DisplayState.MessageState.ConfirmSave)
    }

    fun onClickFaculty(profile: TeacherProfile) {
        _uiState.update {
            it.copy(
                navigationState = TeacherProfileEditUiState.NavigationState.GoToFacultyPickerScreen(
                    mode = FacultySelectionMode.FACULTY,
                    facultyId = profile.teacher.facultyId,
                )
            )
        }
    }

    fun updateProfileScreenState(
        profile: TeacherProfile,
        academicValidationError: TeacherAcademicInfoInputError,
        connectValidationError: TeacherConnectInfoInputError,
        mode: com.workfort.pstuian.featuredomain.model.ProfileEditMode,
    ) {
        val panelState = when (mode) {
            com.workfort.pstuian.featuredomain.model.ProfileEditMode.ACADEMIC -> {
                TeacherProfileEditUiState.DisplayState.PanelState.Academic(
                    profile, academicValidationError
                )
            }
            com.workfort.pstuian.featuredomain.model.ProfileEditMode.CONNECT -> {
                TeacherProfileEditUiState.DisplayState.PanelState.Connect(
                    profile, connectValidationError
                )
            }
        }
        updatePanelState(panelState)
    }

    fun updatePanelState(panelState: TeacherProfileEditUiState.DisplayState.PanelState) {
        _uiState.update {
            it.copy(displayState = it.displayState.copy(panelState = panelState))
        }
    }

    fun updateMessageState(messageState: TeacherProfileEditUiState.DisplayState.MessageState) {
        _uiState.update {
            it.copy(displayState = it.displayState.copy(messageState = messageState))
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
