package com.workfort.pstuian.ui.signup

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.data.infrastructure.repository.FacultyRepositoryImpl
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.model.FacultySelectionMode
import com.workfort.pstuian.featuredomain.model.StudentSignUpInput
import com.workfort.pstuian.featuredomain.model.StudentSignUpInputValidationError
import com.workfort.pstuian.featuredomain.model.TeacherSignUpInput
import com.workfort.pstuian.featuredomain.model.TeacherSignUpInputValidationError
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.ui.signup.state.SignUpMessageState
import com.workfort.pstuian.ui.signup.state.SignUpNavigationState
import com.workfort.pstuian.ui.signup.state.SignUpUiEvent
import com.workfort.pstuian.ui.signup.state.SignUpUiState
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepo: AuthRepository,
    private val facultyRepo: FacultyRepositoryImpl,
    private val stateMachine: SignUpUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<SignUpUiState>(stateMachine) {

    private val _message = MutableStateFlow<SignUpMessageState?>(null)
    val message: StateFlow<SignUpMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<SignUpNavigationState?>(null)
    val navigation: StateFlow<SignUpNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {}

    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.BackClicked -> {
                _navigation.update { SignUpNavigationState.GoBack }
            }
            is SignUpUiEvent.UserTypeBtnClicked -> stateMachine.onClickUserTypeBtn(event.userType)
            is SignUpUiEvent.SignUpStudentClicked -> signUpStudent()
            is SignUpUiEvent.SignUpTeacherClicked -> signUpTeacher()
            is SignUpUiEvent.FacultyClicked -> {
                _navigation.update {
                    SignUpNavigationState.GoToFacultyPickerScreen(
                        mode = stateMachine.getFacultySelectionMode(),
                        facultyId = stateMachine.getSelectedFacultyId(),
                        batchId = null,
                    )
                }
            }
            is SignUpUiEvent.BatchClicked -> {
                _navigation.update {
                    SignUpNavigationState.GoToFacultyPickerScreen(
                        mode = if (stateMachine.getSelectedFacultyId() == null) {
                            FacultySelectionMode.BOTH
                        } else {
                            FacultySelectionMode.BATCH
                        },
                        facultyId = stateMachine.getSelectedFacultyId(),
                        batchId = stateMachine.getSelectedBatchId(),
                    )
                }
            }
            is SignUpUiEvent.SignInClicked -> {
                _navigation.update { SignUpNavigationState.GoBack }
            }
            is SignUpUiEvent.TermsAndConditionsClicked -> Unit // Handled in Screen
            is SignUpUiEvent.PrivacyPolicyClicked -> Unit // Handled in Screen
            is SignUpUiEvent.FacultyChanged -> onChangeFaculty(event.facultyId ?: 0)
            is SignUpUiEvent.BatchChanged -> onChangeBatch(event.batchId ?: 0)
            is SignUpUiEvent.StudentSignUpInputChanged ->
                stateMachine.onChangeStudentSignUpInput(event.signUpInput)
            is SignUpUiEvent.TeacherSignUpInputChanged ->
                stateMachine.onChangeTeacherSignUpInput(event.signUpInput)
        }
    }

    fun onMessageHandled() {
        _message.update { null }
    }

    fun onNavigationConsumed() {
        _navigation.update { null }
    }

    private fun onChangeFaculty(facultyId: Int) {
        val userType = uiState.value.userType
        val currentFacultyId = when (userType) {
            UserType.STUDENT -> uiState.value.studentSignUpInput.faculty?.id
            UserType.TEACHER -> uiState.value.teacherSignUpInput.faculty?.id
            UserType.EMPLOYEE -> uiState.value.teacherSignUpInput.faculty?.id
        }
        if (facultyId == currentFacultyId) return

        stateMachine.updateUiState { it.copy(isLoading = true) }
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            runCatching {
                val faculty = facultyRepo.getFaculty(facultyId)
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
                stateMachine.updateUiState { it.copy(isLoading = false) }
                _message.update { SignUpMessageState.Error(message) }
            }
        }
    }

    private fun onChangeBatch(batchId: Int) {
        if (uiState.value.studentSignUpInput.batch?.id == batchId) return

        stateMachine.updateUiState { it.copy(isLoading = true) }
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            runCatching {
                val batch = facultyRepo.getBatch(batchId)
                val faculty = facultyRepo.getFaculty(batch.facultyId)
                stateMachine.updateUiState {
                    it.copy(
                        isLoading = false,
                        studentSignUpInput = it.studentSignUpInput.copy(
                            faculty = faculty,
                            batch = batch,
                        )
                    )
                }
            }.onFailure {
                val message = it.message ?: "Failed to load batch"
                stateMachine.updateUiState { it.copy(isLoading = false) }
                _message.update { SignUpMessageState.Error(message) }
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
        viewModelScope.launch(coroutineDispatcherProvider.io) {
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
                stateMachine.updateUiState { it.copy(isLoading = false) }
                _message.update { SignUpMessageState.SignUpSuccess }
            }.onFailure {
                val msg = it.message ?: "Failed to Sign up. Please try again."
                stateMachine.updateUiState { it.copy(isLoading = false) }
                _message.update { SignUpMessageState.Error(msg) }
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
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            runCatching {
                authRepo.signUpTeacher(
                    name = input.name,
                    designation = input.designation,
                    department = input.department,
                    email = input.email,
                    password = input.password,
                    facultyId = input.faculty!!.id,
                )
                stateMachine.updateUiState { it.copy(isLoading = false) }
                _message.update { SignUpMessageState.SignUpSuccess }
            }.onFailure {
                val msg = it.message ?: "Failed to Sign up. Please try again."
                stateMachine.updateUiState { it.copy(isLoading = false) }
                _message.update { SignUpMessageState.Error(msg) }
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
