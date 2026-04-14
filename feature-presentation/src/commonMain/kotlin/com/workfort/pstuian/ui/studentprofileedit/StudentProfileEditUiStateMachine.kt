package com.workfort.pstuian.ui.studentprofileedit

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.FacultySelectionMode
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
    private val _uiState = MutableStateFlow(StudentProfileEditUiState())
    override val uiState: StateFlow<StudentProfileEditUiState> = _uiState

    fun messageConsumed() {
        _uiState.update { it.copy(displayState = it.displayState.copy(messageState = null)) }
    }

    fun navigationConsumed() {
        _uiState.update { it.copy(navigationState = null) }
    }

    fun onClickBack() {
        _uiState.update {
            it.copy(navigationState = StudentProfileEditUiState.NavigationState.GoBack)
        }
    }

    fun onClickSave() {
        updateMessageState(StudentProfileEditUiState.DisplayState.MessageState.ConfirmSave)
    }

    fun onClickFaculty(profile: StudentProfile) {
        _uiState.update {
            it.copy(
                navigationState = StudentProfileEditUiState.NavigationState.GoToFacultyPickerScreen(
                    mode = FacultySelectionMode.BOTH,
                    facultyId = profile.student.facultyId,
                    batchId = profile.student.batchId,
                )
            )
        }
    }

    fun onClickBatch(profile: StudentProfile) {
        _uiState.update {
            it.copy(
                navigationState = StudentProfileEditUiState.NavigationState.GoToFacultyPickerScreen(
                    mode = FacultySelectionMode.BATCH,
                    facultyId = profile.student.facultyId,
                    batchId = profile.student.batchId,
                )
            )
        }
    }

    fun updateProfileScreenState(
        profile: StudentProfile,
        academicValidationError: StudentAcademicInfoInputError,
        connectValidationError: StudentConnectInfoInputError,
        mode: ProfileEditMode,
    ) {
        val panelState = when (mode) {
            ProfileEditMode.ACADEMIC -> {
                StudentProfileEditUiState.DisplayState.PanelState.Academic(
                    profile, academicValidationError
                )
            }
            ProfileEditMode.CONNECT -> {
                StudentProfileEditUiState.DisplayState.PanelState.Connect(
                    profile, connectValidationError
                )
            }
        }
        updatePanelState(panelState)
    }

    fun updatePanelState(panelState: StudentProfileEditUiState.DisplayState.PanelState) {
        _uiState.update {
            it.copy(displayState = it.displayState.copy(panelState = panelState))
        }
    }

    fun updateMessageState(messageState: StudentProfileEditUiState.DisplayState.MessageState) {
        _uiState.update {
            it.copy(displayState = it.displayState.copy(messageState = messageState))
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
