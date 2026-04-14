package com.workfort.pstuian.ui.signup

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.data.infrastructure.repository.FacultyRepositoryImpl
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.model.FacultySelectionMode
import com.workfort.pstuian.model.StudentSignUpInput
import com.workfort.pstuian.model.StudentSignUpInputValidationError
import com.workfort.pstuian.model.TeacherSignUpInput
import com.workfort.pstuian.model.TeacherSignUpInputValidationError
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignUpUiStateMachine : UiStateMachine<SignUpUiState> {
    private val _uiState = MutableStateFlow(SignUpUiState())
    override val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun updateUiState(updater: (SignUpUiState) -> SignUpUiState) {
        _uiState.update(updater)
    }

    fun messageConsumed() = _uiState.update { it.copy(messageState = null) }

    fun navigationConsumed() = _uiState.update { it.copy(navigationState = null) }

    fun onClickBack() = _uiState.update { it.copy(navigationState = NavigationState.GoBack) }

    fun onClickUserTypeBtn(userType: UserType) {
        _uiState.update { it.copy(userType = userType) }
    }

    fun onClickFaculty() {
        val userType = _uiState.value.userType
        val (selectionMode, facultyId) = when (userType) {
            UserType.STUDENT -> {
                FacultySelectionMode.BOTH to _uiState.value.studentSignUpInput.faculty?.id
            }
            UserType.TEACHER -> {
                FacultySelectionMode.FACULTY to _uiState.value.teacherSignUpInput.faculty?.id
            }
            else -> FacultySelectionMode.FACULTY to null
        }
        _uiState.update {
            it.copy(
                navigationState = NavigationState.GoToFacultyPickerScreen(
                    mode = selectionMode,
                    facultyId = facultyId,
                    batchId = null,
                )
            )
        }
    }

    fun onClickBatch() {
        val userType = _uiState.value.userType
        val (facultyId, batchId) = when (userType) {
            UserType.STUDENT -> {
                _uiState.value.studentSignUpInput.faculty?.id to _uiState.value.studentSignUpInput.batch?.id
            }
            UserType.TEACHER -> {
                _uiState.value.teacherSignUpInput.faculty?.id to null
            }
            else -> null to null
        }
        _uiState.update {
            it.copy(
                navigationState = NavigationState.GoToFacultyPickerScreen(
                    mode = if (facultyId == null) {
                        FacultySelectionMode.BOTH
                    } else {
                        FacultySelectionMode.BATCH
                    },
                    facultyId = facultyId,
                    batchId = batchId,
                )
            )
        }
    }

    fun onClickSignIn() = _uiState.update { it.copy(navigationState = NavigationState.GoBack) }

    fun onChangeStudentSignUpInput(signUpInput: StudentSignUpInput) {
        _uiState.update { it.copy(studentSignUpInput = signUpInput) }
    }

    fun onChangeTeacherSignUpInput(signUpInput: TeacherSignUpInput) {
        _uiState.update { it.copy(teacherSignUpInput = signUpInput) }
    }
}
