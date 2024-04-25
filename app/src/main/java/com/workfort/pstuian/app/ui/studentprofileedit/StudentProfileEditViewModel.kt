package com.workfort.pstuian.app.ui.studentprofileedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.FacultySelectionMode
import com.workfort.pstuian.model.ProfileEditMode
import com.workfort.pstuian.model.StudentAcademicInfoInputError
import com.workfort.pstuian.model.StudentConnectInfoInputError
import com.workfort.pstuian.model.StudentProfile
import com.workfort.pstuian.repository.FacultyRepository
import com.workfort.pstuian.repository.StudentRepository
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class StudentProfileEditViewModel(
    private val userId: Int,
    private val mode: ProfileEditMode,
    private val studentRepo: StudentRepository,
    private val facultyRepo: FacultyRepository,
    private val stateReducer: StudentProfileEditScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(stateReducer.initial)
    val screenState: StateFlow<StudentProfileEditScreenState> get() = _screenState

    private fun updateScreenState(update: StudentProfileEditScreenStateUpdate) =
        _screenState.update { oldState -> stateReducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(StudentProfileEditScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(StudentProfileEditScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        StudentProfileEditScreenStateUpdate.NavigateTo(
            StudentProfileEditScreenState.NavigationState.GoBack,
        ),
    )

    fun onClickSave() {
        updateScreenState(
            StudentProfileEditScreenStateUpdate.UpdateMessageState(
                StudentProfileEditScreenState.DisplayState.MessageState.ConfirmSave,
            ),
        )
    }

    fun onClickFaculty() = newProfileCache?.student?.let { student ->
        updateScreenState(
            StudentProfileEditScreenStateUpdate.NavigateTo(
                StudentProfileEditScreenState.NavigationState.GoToFacultyPickerScreen(
                    mode = FacultySelectionMode.BOTH,
                    facultyId = student.facultyId,
                    batchId = student.batchId,
                ),
            ),
        )
    }

    fun onClickBatch() = newProfileCache?.student?.let { student ->
        updateScreenState(
            StudentProfileEditScreenStateUpdate.NavigateTo(
                StudentProfileEditScreenState.NavigationState.GoToFacultyPickerScreen(
                    mode = FacultySelectionMode.BATCH,
                    facultyId = student.facultyId,
                    batchId = student.batchId,
                ),
            ),
        )
    }

    fun onChangeFaculty(facultyId: Int) {
        if (newProfileCache?.faculty?.id == facultyId) return
        updateScreenState(
            StudentProfileEditScreenStateUpdate.UpdateMessageState(
                StudentProfileEditScreenState.DisplayState.MessageState.Loading(cancelable = false),
            ),
        )
        viewModelScope.launch {
            runCatching {
                val faculty = facultyRepo.getFaculty(facultyId)
                newProfileCache?.let {
                    val newProfile = it.copy(
                        student = it.student.copy(facultyId = faculty.id),
                        faculty = faculty,
                    )
                    newProfileCache = newProfile
                    messageConsumed()
                    updateProfileScreenState()
                }
            }.onFailure {
                val message = it.message ?: "Failed to load faculty"
                updateScreenState(
                    StudentProfileEditScreenStateUpdate.UpdateMessageState(
                        StudentProfileEditScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }

    fun onChangeBatch(batchId: Int) {
        if (newProfileCache?.batch?.id == batchId) return
        updateScreenState(
            StudentProfileEditScreenStateUpdate.UpdateMessageState(
                StudentProfileEditScreenState.DisplayState.MessageState.Loading(cancelable = false),
            ),
        )
        viewModelScope.launch {
            runCatching {
                val batch = facultyRepo.getBatch(batchId)
                val faculty = facultyRepo.getFaculty(batch.facultyId)
                newProfileCache?.let {
                    val newProfile = it.copy(
                        student = it.student.copy(batchId = batch.id, facultyId = faculty.id),
                        batch = batch,
                        faculty = faculty,
                    )
                    newProfileCache = newProfile
                    messageConsumed()
                    updateProfileScreenState()
                }
            }.onFailure {
                val message = it.message ?: "Failed to load batch"
                updateScreenState(
                    StudentProfileEditScreenStateUpdate.UpdateMessageState(
                        StudentProfileEditScreenState.DisplayState.MessageState.Error(
                            message,
                        )
                    ),
                )
            }
        }
    }

    fun onChangeProfile(profile: StudentProfile) {
        newProfileCache = profile
        when (mode) {
            ProfileEditMode.ACADEMIC -> newProfileCache?.validateAcademic()?.let {
                academicValidationError = it
            }
            ProfileEditMode.CONNECT -> newProfileCache?.validateConnect()?.let {
                connectValidationError = it
            }
        }
        updateProfileScreenState()
    }

    private var oldProfileCache: StudentProfile? = null
    private var newProfileCache: StudentProfile? = null
    private var academicValidationError = StudentAcademicInfoInputError.INITIAL
    private var connectValidationError = StudentConnectInfoInputError.INITIAL
    fun loadProfile() {
        updateScreenState(StudentProfileEditScreenStateUpdate.ProfileLoading)
        viewModelScope.launch {
            runCatching {
                oldProfileCache = studentRepo.getProfile(userId)
                newProfileCache = oldProfileCache
                when (mode) {
                    ProfileEditMode.ACADEMIC -> newProfileCache?.validateAcademic()?.let {
                        academicValidationError = it
                    }
                    ProfileEditMode.CONNECT -> newProfileCache?.validateConnect()?.let {
                        connectValidationError = it
                    }
                }
                updateProfileScreenState()
            }.onFailure {
                val message = it.message ?: "Failed to load profile"
                messageConsumed()
                updateScreenState(StudentProfileEditScreenStateUpdate.ProfileLoadFailed(message))
            }
        }
    }

    fun updateProfile() {
        if (academicValidationError.isNotEmpty() || connectValidationError.isNotEmpty()) {
            updateScreenState(
                StudentProfileEditScreenStateUpdate.UpdateMessageState(
                    StudentProfileEditScreenState.DisplayState.MessageState.Error(
                        "Please insert required fields and try again!"
                    ),
                ),
            )
            return
        }
        updateScreenState(
            StudentProfileEditScreenStateUpdate.UpdateMessageState(
                StudentProfileEditScreenState.DisplayState.MessageState.Loading(cancelable = false),
            ),
        )
        val oldProfile = oldProfileCache ?: return
        val newProfile = newProfileCache ?: return
        viewModelScope.launch {
            runCatching {
                when (mode) {
                    ProfileEditMode.ACADEMIC -> {
                        studentRepo.changeAcademicInfo(
                            student = oldProfile.student,
                            name = newProfile.student.name,
                            id = newProfile.student.id,
                            reg = newProfile.student.reg,
                            blood = newProfile.student.blood.orEmpty(),
                            facultyId = newProfile.student.facultyId,
                            session = newProfile.student.session,
                            batchId = newProfile.student.batchId,
                        )
                    }
                    ProfileEditMode.CONNECT -> {
                        studentRepo.changeConnectInfo(
                            student = oldProfile.student,
                            address = newProfile.student.address.orEmpty(),
                            phone = newProfile.student.phone.orEmpty(),
                            email = newProfile.student.email.orEmpty(),
                            cvLink = newProfile.student.cvLink.orEmpty(),
                            linkedIn = newProfile.student.linkedIn.orEmpty(),
                            facebook = newProfile.student.fbLink.orEmpty(),
                        )
                    }
                }
            }.onSuccess {
                updateScreenState(
                    StudentProfileEditScreenStateUpdate.UpdateMessageState(
                        StudentProfileEditScreenState.DisplayState.MessageState.Success(
                            "Profile updated successfully!",
                        ),
                    ),
                )
                loadProfile()
            }.onFailure {
                val message = it.message ?: "Failed to update. Please try again."
                updateScreenState(
                    StudentProfileEditScreenStateUpdate.UpdateMessageState(
                        StudentProfileEditScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }

    private fun updateProfileScreenState() {
        val profile = newProfileCache ?: return
        val state = when (mode) {
            ProfileEditMode.ACADEMIC -> {
                StudentProfileEditScreenStateUpdate.ProfileLoadedForAcademic(
                    profile, academicValidationError
                )
            }
            ProfileEditMode.CONNECT -> {
                StudentProfileEditScreenStateUpdate.ProfileLoadedForConnect(
                    profile, connectValidationError
                )
            }
        }
        updateScreenState(state)
    }

    private fun StudentProfile.validateAcademic() = StudentAcademicInfoInputError.INITIAL.copy(
        name = if (student.name.isEmpty()) "*Required" else "",
        id = if (student.id == 0) "*Required" else "",
        reg = if (student.reg.isEmpty()) "*Required" else "",
        session = if (student.session.isEmpty()) "*Required" else "",
    )

    private fun StudentProfile.validateConnect() = StudentConnectInfoInputError.INITIAL.copy(
        email = if (student.email.isNullOrEmpty()) {
            "*Required"
        } else if (student.email?.isValidEmail() == false) {
            "*Invalid email"
        } else {
            ""
        },
    )
}