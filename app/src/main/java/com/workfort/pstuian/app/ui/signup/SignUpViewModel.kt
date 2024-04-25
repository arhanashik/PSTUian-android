package com.workfort.pstuian.app.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.FacultySelectionMode
import com.workfort.pstuian.model.StudentSignUpInput
import com.workfort.pstuian.model.StudentSignUpInputValidationError
import com.workfort.pstuian.model.TeacherSignUpInput
import com.workfort.pstuian.model.TeacherSignUpInputValidationError
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.repository.AuthRepository
import com.workfort.pstuian.repository.FacultyRepository
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SignUpViewModel (
    private val authRepo: AuthRepository,
    private val facultyRepo: FacultyRepository,
    private val reducer: SignUpScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<SignUpScreenState> get() = _screenState

    private fun updateScreenState(update: SignUpScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(SignUpScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(SignUpScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        SignUpScreenStateUpdate.NavigateTo(
            SignUpScreenState.NavigationState.GoBack,
        ),
    )

    fun onClickUserTypeBtn(userType: UserType) {
        val newPanelState = when (userType) {
            UserType.STUDENT -> SignUpScreenStateUpdate.SetPanelToStudentSignUp(
                studentSignUpInput,
                studentSignUpInputValidationError,
            )
            UserType.TEACHER -> SignUpScreenStateUpdate.SetPanelToTeacherSignUp(
                teacherSignUpInput,
                teacherSignUpInputValidationError,
            )
        }
        updateScreenState(newPanelState)
    }

    fun onClickFaculty() {
        getCurrentUserType()?.let { userType ->
            val (selectionMode, facultyId) = when (userType) {
                UserType.STUDENT -> {
                    FacultySelectionMode.BOTH to studentSignUpInput.faculty?.id
                }
                UserType.TEACHER -> {
                    FacultySelectionMode.FACULTY to teacherSignUpInput.faculty?.id
                }
            }
            updateScreenState(
                SignUpScreenStateUpdate.NavigateTo(
                    SignUpScreenState.NavigationState.GoToFacultyPickerScreen(
                        mode = selectionMode,
                        facultyId = facultyId,
                        batchId = null,
                    ),
                ),
            )
        }
    }

    fun onClickBatch() {
        getCurrentUserType()?.let { userType ->
            val (facultyId, batchId) = when (userType) {
                UserType.STUDENT -> {
                    studentSignUpInput.faculty?.id to studentSignUpInput.batch?.id
                }
                UserType.TEACHER -> {
                    teacherSignUpInput.faculty?.id to null
                }
            }
            updateScreenState(
                SignUpScreenStateUpdate.NavigateTo(
                    SignUpScreenState.NavigationState.GoToFacultyPickerScreen(
                        mode = if(facultyId == null) {
                            FacultySelectionMode.BOTH
                        } else {
                            FacultySelectionMode.BATCH
                        },
                        facultyId = facultyId,
                        batchId = batchId,
                    ),
                ),
            )
        }
    }

    fun onClickSignIn() = updateScreenState(
        SignUpScreenStateUpdate.NavigateTo(
            SignUpScreenState.NavigationState.GoBack,
        ),
    )

    fun onChangeFaculty(facultyId: Int) {
        val userType = getCurrentUserType() ?: return
        val currentFacultyId = when (userType) {
            UserType.STUDENT -> studentSignUpInput.faculty?.id
            UserType.TEACHER -> teacherSignUpInput.faculty?.id
        }
        if (facultyId == currentFacultyId) return

        viewModelScope.launch {
            updateScreenState(SignUpScreenStateUpdate.ShowLoading(true))
            runCatching {
                val faculty = facultyRepo.getFaculty(facultyId)
                val newPanelState = when (userType) {
                    UserType.STUDENT -> {
                        studentSignUpInput = studentSignUpInput.copy(faculty = faculty)
                        SignUpScreenStateUpdate.SetPanelToStudentSignUp(
                            studentSignUpInput,
                            studentSignUpInputValidationError,
                        )
                    }
                    UserType.TEACHER -> {
                        teacherSignUpInput = teacherSignUpInput.copy(faculty = faculty)
                        SignUpScreenStateUpdate.SetPanelToTeacherSignUp(
                            teacherSignUpInput,
                            teacherSignUpInputValidationError,
                        )
                    }
                }
                updateScreenState(newPanelState)
            }.onFailure {
                val message = it.message ?: "Failed to load faculty"
                updateScreenState(
                    SignUpScreenStateUpdate.UpdateMessageState(
                        SignUpScreenState.DisplayState.MessageState.Error(message)
                    ),
                )
            }
        }
    }

    fun onChangeBatch(batchId: Int) {
        if (studentSignUpInput.batch?.id == batchId) return

        viewModelScope.launch {
            updateScreenState(SignUpScreenStateUpdate.ShowLoading(true))
            runCatching {
                val batch = facultyRepo.getBatch(batchId)
                val faculty = facultyRepo.getFaculty(batch.facultyId)
                studentSignUpInput = studentSignUpInput.copy(
                    faculty = faculty,
                    batch = batch,
                )
                updateScreenState(
                    SignUpScreenStateUpdate.SetPanelToStudentSignUp(
                        studentSignUpInput,
                        studentSignUpInputValidationError,
                    )
                )
            }.onFailure {
                val message = it.message ?: "Failed to load batch"
                updateScreenState(
                    SignUpScreenStateUpdate.UpdateMessageState(
                        SignUpScreenState.DisplayState.MessageState.Error(message)
                    ),
                )
            }
        }
    }

    fun onChangeStudentSignUpInput(currentSignUpInfo: StudentSignUpInput) {
        studentSignUpInput = currentSignUpInfo
    }

    fun onChangeTeacherSignUpInput(currentSignUpInfo: TeacherSignUpInput) {
        teacherSignUpInput = currentSignUpInfo
    }

    private fun getCurrentUserType() : UserType? {
        return when (_screenState.value.displayState.panelState) {
            is SignUpScreenState.DisplayState.PanelState.None -> null
            is SignUpScreenState.DisplayState.PanelState.StudentSignUp -> UserType.STUDENT
            is SignUpScreenState.DisplayState.PanelState.TeacherSignUp -> UserType.TEACHER
        }
    }

    private var studentSignUpInput = StudentSignUpInput.INITIAL
    private var studentSignUpInputValidationError = StudentSignUpInputValidationError.INITIAL
    fun signUpStudent() {
        studentSignUpInputValidationError = studentSignUpInput.validate()
        updateScreenState(
            SignUpScreenStateUpdate.SetPanelToStudentSignUp(
                studentSignUpInput, studentSignUpInputValidationError,
            )
        )
        if (studentSignUpInputValidationError.isNotEmpty()) {
            return
        }
        viewModelScope.launch {
            updateScreenState(SignUpScreenStateUpdate.ShowLoading(true))
            runCatching {
                authRepo.signUpStudent(
                    name = studentSignUpInput.name,
                    id = studentSignUpInput.id,
                    reg = studentSignUpInput.reg,
                    facultyId = studentSignUpInput.faculty!!.id,
                    batchId = studentSignUpInput.batch!!.id,
                    session = studentSignUpInput.session,
                    email = studentSignUpInput.email,
                )
                updateScreenState(SignUpScreenStateUpdate.ShowLoading(false))
                updateScreenState(
                    SignUpScreenStateUpdate.UpdateMessageState(
                        SignUpScreenState.DisplayState.MessageState.SignUpSuccess,
                    ),
                )
            }.onFailure {
                val msg = it.message?: "Failed to Sign up. Please try again."
                updateScreenState(SignUpScreenStateUpdate.ShowLoading(false))
                updateScreenState(
                    SignUpScreenStateUpdate.UpdateMessageState(
                        SignUpScreenState.DisplayState.MessageState.Error(msg)
                    ),
                )
            }
        }
    }

    private var teacherSignUpInput = TeacherSignUpInput.INITIAL
    private var teacherSignUpInputValidationError = TeacherSignUpInputValidationError.INITIAL
    fun signUpTeacher() {
        teacherSignUpInputValidationError = teacherSignUpInput.validate()
        updateScreenState(
            SignUpScreenStateUpdate.SetPanelToTeacherSignUp(
                teacherSignUpInput, teacherSignUpInputValidationError,
            )
        )
        if (teacherSignUpInputValidationError.isNotEmpty()) {
            return
        }
        viewModelScope.launch {
            updateScreenState(SignUpScreenStateUpdate.ShowLoading(true))
            runCatching {
                authRepo.signUpTeacher(
                    name = teacherSignUpInput.name,
                    designation = teacherSignUpInput.designation,
                    department = teacherSignUpInput.department,
                    email = teacherSignUpInput.email,
                    password = teacherSignUpInput.password,
                    facultyId = teacherSignUpInput.faculty!!.id,
                )
                updateScreenState(SignUpScreenStateUpdate.ShowLoading(false))
                updateScreenState(
                    SignUpScreenStateUpdate.UpdateMessageState(
                        SignUpScreenState.DisplayState.MessageState.SignUpSuccess,
                    ),
                )
            }.onFailure {
                val msg = it.message?: "Failed to Sign up. Please try again."
                updateScreenState(SignUpScreenStateUpdate.ShowLoading(false))
                updateScreenState(
                    SignUpScreenStateUpdate.UpdateMessageState(
                        SignUpScreenState.DisplayState.MessageState.Error(msg)
                    ),
                )
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
        }  else if (reg.toIntOrNull() == null) {
            "*Only numbers are allowed"
        } else {
            ""
        },
        session = if (session.isEmpty()) "*Required" else "",
        faculty = if (faculty == null) "*Required" else "",
        batch = if (batch == null) "*Required" else "",
        email = if (email.isEmpty()) {
            "*Required"
        }  else if (email.isValidEmail().not()) {
            "*Invalid email address"
        } else {
            ""
        },
        password = if (password.isEmpty()) {
            "*Required"
        }  else if (password.length < 4) {
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
        }  else if (email.isValidEmail().not()) {
            "*Invalid email address"
        } else {
            ""
        },
        password = if (password.isEmpty()) {
            "*Required"
        }  else if (password.length < 4) {
            "*Password too short"
        } else {
            ""
        },
    )
}