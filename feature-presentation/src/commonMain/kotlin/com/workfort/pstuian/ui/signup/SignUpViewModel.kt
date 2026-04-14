package com.workfort.pstuian.ui.signup

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.data.infrastructure.repository.FacultyRepositoryImpl
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.model.FacultySelectionMode
import com.workfort.pstuian.model.StudentSignUpInput
import com.workfort.pstuian.model.StudentSignUpInputValidationError
import com.workfort.pstuian.model.TeacherSignUpInput
import com.workfort.pstuian.model.TeacherSignUpInputValidationError
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepo: AuthRepository,
    private val facultyRepo: FacultyRepositoryImpl,
    private val stateMachine: SignUpUiStateMachine,
) : UiStateMachineViewModel<SignUpUiState>(stateMachine) {

    override fun onUiReady() {}

    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.None -> Unit
            is SignUpUiEvent.OnClickBack -> stateMachine.onClickBack()
            is SignUpUiEvent.OnClickUserTypeBtn -> stateMachine.onClickUserTypeBtn(event.userType)
            is SignUpUiEvent.OnClickSignUpStudent -> signUpStudent()
            is SignUpUiEvent.OnClickSignUpTeacher -> signUpTeacher()
            is SignUpUiEvent.OnClickFaculty -> stateMachine.onClickFaculty()
            is SignUpUiEvent.OnClickBatch -> stateMachine.onClickBatch()
            is SignUpUiEvent.OnClickSignIn -> stateMachine.onClickSignIn()
            is SignUpUiEvent.OnClickTermsAndConditions -> Unit // Handled in Screen
            is SignUpUiEvent.OnClickPrivacyPolicy -> Unit // Handled in Screen
            is SignUpUiEvent.OnChangeFaculty -> onChangeFaculty(event.facultyId ?: 0)
            is SignUpUiEvent.OnChangeBatch -> onChangeBatch(event.batchId ?: 0)
            is SignUpUiEvent.OnChangeStudentSignUpInput ->
                stateMachine.onChangeStudentSignUpInput(event.signUpInput)
            is SignUpUiEvent.OnChangeTeacherSignUpInput ->
                stateMachine.onChangeTeacherSignUpInput(event.signUpInput)
            is SignUpUiEvent.MessageConsumed -> stateMachine.messageConsumed()
            is SignUpUiEvent.NavigationConsumed -> stateMachine.navigationConsumed()
        }
    }

    private fun onChangeFaculty(facultyId: Int) {
        val userType = uiState.value.userType
        val currentFacultyId = when (userType) {
            UserType.STUDENT -> uiState.value.studentSignUpInput.faculty?.id
            UserType.TEACHER -> uiState.value.teacherSignUpInput.faculty?.id
            else -> null
        }
        if (facultyId == currentFacultyId) return

        stateMachine.updateUiState { it.copy(isLoading = true) }
        viewModelScope.launch {
            runCatching {
                val faculty = facultyRepo.getFaculty(facultyId).toDto()
                stateMachine.updateUiState {
                    it.copy(
                        isLoading = false,
                        studentSignUpInput = if (userType == UserType.STUDENT) {
                            it.studentSignUpInput.copy(faculty = faculty)
                        } else {
                            it.studentSignUpInput
                        },
                        teacherSignUpInput = if (userType == UserType.TEACHER) {
                            it.teacherSignUpInput.copy(faculty = faculty)
                        } else {
                            it.teacherSignUpInput
                        }
                    )
                }
            }.onFailure {
                val message = it.message ?: "Failed to load faculty"
                stateMachine.updateUiState { state ->
                    state.copy(
                        isLoading = false,
                        messageState = MessageState.Error(message)
                    )
                }
            }
        }
    }

    private fun onChangeBatch(batchId: Int) {
        if (uiState.value.studentSignUpInput.batch?.id == batchId) return

        stateMachine.updateUiState { it.copy(isLoading = true) }
        viewModelScope.launch {
            runCatching {
                val batch = facultyRepo.getBatch(batchId)
                val faculty = facultyRepo.getFaculty(batch.facultyId)
                stateMachine.updateUiState {
                    it.copy(
                        isLoading = false,
                        studentSignUpInput = it.studentSignUpInput.copy(
                            faculty = faculty.toDto(),
                            batch = batch.toDto(),
                        )
                    )
                }
            }.onFailure {
                val message = it.message ?: "Failed to load batch"
                stateMachine.updateUiState { state ->
                    state.copy(
                        isLoading = false,
                        messageState = MessageState.Error(message)
                    )
                }
            }
        }
    }

    private fun signUpStudent() {
        val input = uiState.value.studentSignUpInput
        val validationError = input.validate()
        stateMachine.updateUiState {
            it.copy(studentSignUpInputValidationError = validationError)
        }
        if (validationError.isNotEmpty()) {
            return
        }
        stateMachine.updateUiState { it.copy(isLoading = true) }
        viewModelScope.launch {
            runCatching {
                authRepo.signUpStudent(
                    name = input.name,
                    id = input.id,
                    reg = input.reg,
                    facultyId = input.faculty!!.id,
                    batchId = input.batch!!.id,
                    session = input.session,
                    email = input.email,
                    password = input.password,
                )
                stateMachine.updateUiState {
                    it.copy(
                        isLoading = false,
                        messageState = MessageState.SignUpSuccess
                    )
                }
            }.onFailure {
                val msg = it.message ?: "Failed to Sign up. Please try again."
                stateMachine.updateUiState { state ->
                    state.copy(
                        isLoading = false,
                        messageState = MessageState.Error(msg)
                    )
                }
            }
        }
    }

    private fun signUpTeacher() {
        val input = uiState.value.teacherSignUpInput
        val validationError = input.validate()
        stateMachine.updateUiState {
            it.copy(teacherSignUpInputValidationError = validationError)
        }
        if (validationError.isNotEmpty()) {
            return
        }
        stateMachine.updateUiState { it.copy(isLoading = true) }
        viewModelScope.launch {
            runCatching {
                authRepo.signUpTeacher(
                    name = input.name,
                    designation = input.designation,
                    department = input.department,
                    email = input.email,
                    password = input.password,
                    facultyId = input.faculty!!.id,
                )
                stateMachine.updateUiState {
                    it.copy(
                        isLoading = false,
                        messageState = MessageState.SignUpSuccess
                    )
                }
            }.onFailure {
                val msg = it.message ?: "Failed to Sign up. Please try again."
                stateMachine.updateUiState { state ->
                    state.copy(
                        isLoading = false,
                        messageState = MessageState.Error(msg)
                    )
                }
            }
        }
    }

    private fun StudentSignUpInput.validate() = StudentSignUpInputValidationError(
        name = if (name.isEmpty()) "*Required" else "",
        id = if (id.isEmpty()) {
            "*Required"
        } else if (id.toIntOrNull() == null) {
            "*Only numbers are allowed"
        } else {
            ""
        },
        reg = if (reg.isEmpty()) {
            "*Required"
        } else if (reg.toIntOrNull() == null) {
            "*Only numbers are allowed"
        } else {
            ""
        },
        session = if (session.isEmpty()) "*Required" else "",
        faculty = if (faculty == null) "*Required" else "",
        batch = if (batch == null) "*Required" else "",
        email = if (email.isEmpty()) {
            "*Required"
        } else if (email.isValidEmail().not()) {
            "*Invalid email address"
        } else {
            ""
        },
        password = if (password.isEmpty()) {
            "*Required"
        } else if (password.length < 4) {
            "*Password too short"
        } else {
            ""
        },
    )

    private fun TeacherSignUpInput.validate() = TeacherSignUpInputValidationError(
        name = if (name.isEmpty()) "*Required" else "",
        designation = if (designation.isEmpty()) {
            "*Required"
        } else {
            ""
        },
        department = if (department.isEmpty()) {
            "*Required"
        } else {
            ""
        },
        faculty = if (faculty == null) "*Required" else "",
        email = if (email.isEmpty()) {
            "*Required"
        } else if (email.isValidEmail().not()) {
            "*Invalid email address"
        } else {
            ""
        },
        password = if (password.isEmpty()) {
            "*Required"
        } else if (password.length < 4) {
            "*Password too short"
        } else {
            ""
        },
    )
}
