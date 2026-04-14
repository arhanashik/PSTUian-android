package com.workfort.pstuian.ui.signup

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.FacultySelectionMode
import com.workfort.pstuian.model.StudentSignUpInput
import com.workfort.pstuian.model.TeacherSignUpInput
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.ui.signup.state.SignUpUiState
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

    fun onClickUserTypeBtn(userType: UserType) {
        _uiState.update { it.copy(userType = userType) }
    }

    fun getFacultySelectionMode(): FacultySelectionMode {
        val userType = _uiState.value.userType
        return when (userType) {
            UserType.STUDENT -> FacultySelectionMode.BOTH
            UserType.TEACHER -> FacultySelectionMode.FACULTY
            else -> FacultySelectionMode.FACULTY
        }
    }

    fun getSelectedFacultyId(): Int? {
        val userType = _uiState.value.userType
        return when (userType) {
            UserType.STUDENT -> _uiState.value.studentSignUpInput.faculty?.id
            UserType.TEACHER -> _uiState.value.teacherSignUpInput.faculty?.id
            else -> null
        }
    }

    fun getSelectedBatchId(): Int? {
        val userType = _uiState.value.userType
        return when (userType) {
            UserType.STUDENT -> _uiState.value.studentSignUpInput.batch?.id
            else -> null
        }
    }

    fun onChangeStudentSignUpInput(signUpInput: StudentSignUpInput) {
        _uiState.update { it.copy(studentSignUpInput = signUpInput) }
    }

    fun onChangeTeacherSignUpInput(signUpInput: TeacherSignUpInput) {
        _uiState.update { it.copy(teacherSignUpInput = signUpInput) }
    }
}
