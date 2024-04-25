package com.workfort.pstuian.app.ui.teacherprofileedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.FacultySelectionMode
import com.workfort.pstuian.model.ProfileEditMode
import com.workfort.pstuian.model.TeacherAcademicInfoInputError
import com.workfort.pstuian.model.TeacherConnectInfoInputError
import com.workfort.pstuian.model.TeacherProfile
import com.workfort.pstuian.repository.FacultyRepository
import com.workfort.pstuian.repository.TeacherRepository
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TeacherProfileEditViewModel(
    private val userId: Int,
    private val mode: ProfileEditMode,
    private val teacherRepo: TeacherRepository,
    private val facultyRepo: FacultyRepository,
    private val stateReducer: TeacherProfileEditScreenStateReducer,
) : ViewModel() {

    private var updatedProfileCache: TeacherProfile? = null

    private val _screenState = MutableStateFlow(stateReducer.initial)
    val screenState: StateFlow<TeacherProfileEditScreenState> get() = _screenState

    private fun updateScreenState(update: TeacherProfileEditScreenStateUpdate) =
        _screenState.update { oldState -> stateReducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(TeacherProfileEditScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(TeacherProfileEditScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        TeacherProfileEditScreenStateUpdate.NavigateTo(
            TeacherProfileEditScreenState.NavigationState.GoBack,
        ),
    )

    fun onClickSave() {
        updateScreenState(
            TeacherProfileEditScreenStateUpdate.UpdateMessageState(
                TeacherProfileEditScreenState.DisplayState.MessageState.ConfirmSave,
            ),
        )
    }

    fun onClickFaculty() = updatedProfileCache?.teacher?.let { teacher ->
        updateScreenState(
            TeacherProfileEditScreenStateUpdate.NavigateTo(
                TeacherProfileEditScreenState.NavigationState.GoToFacultyPickerScreen(
                    mode = FacultySelectionMode.FACULTY,
                    facultyId = teacher.facultyId,
                ),
            ),
        )
    }

    fun onChangeFaculty(facultyId: Int) {
        if (newProfileCache?.faculty?.id == facultyId) return
        updateScreenState(
            TeacherProfileEditScreenStateUpdate.UpdateMessageState(
                TeacherProfileEditScreenState.DisplayState.MessageState.Loading(cancelable = false),
            ),
        )
        viewModelScope.launch {
            runCatching {
                val faculty = facultyRepo.getFaculty(facultyId)
                newProfileCache?.let {
                    val newProfile = it.copy(
                        teacher = it.teacher.copy(facultyId = faculty.id),
                        faculty = faculty,
                    )
                    newProfileCache = newProfile
                    messageConsumed()
                    updateProfileScreenState()
                }
            }.onFailure {
                val message = it.message ?: "Failed to load faculty"
                updateScreenState(
                    TeacherProfileEditScreenStateUpdate.UpdateMessageState(
                        TeacherProfileEditScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }

    fun onChangeProfile(profile: TeacherProfile) {
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

    private var oldProfileCache: TeacherProfile? = null
    private var newProfileCache: TeacherProfile? = null
    private var academicValidationError = TeacherAcademicInfoInputError.INITIAL
    private var connectValidationError = TeacherConnectInfoInputError.INITIAL
    fun loadProfile() {
        updateScreenState(TeacherProfileEditScreenStateUpdate.ProfileLoading)
        viewModelScope.launch {
            runCatching {
                oldProfileCache = teacherRepo.getProfile(userId)
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
                updateScreenState(TeacherProfileEditScreenStateUpdate.ProfileLoadFailed(message))
            }
        }
    }

    fun updateProfile() {
        if (academicValidationError.isNotEmpty() || connectValidationError.isNotEmpty()) {
            updateScreenState(
                TeacherProfileEditScreenStateUpdate.UpdateMessageState(
                    TeacherProfileEditScreenState.DisplayState.MessageState.Error(
                        "Please insert required fields and try again!"
                    ),
                ),
            )
            return
        }
        updateScreenState(
            TeacherProfileEditScreenStateUpdate.UpdateMessageState(
                TeacherProfileEditScreenState.DisplayState.MessageState.Loading(cancelable = false),
            ),
        )
        val oldProfile = oldProfileCache ?: return
        val newProfile = newProfileCache ?: return
        viewModelScope.launch {
            runCatching {
                when (mode) {
                    ProfileEditMode.ACADEMIC -> {
                        teacherRepo.changeAcademicInfo(
                            teacher = oldProfile.teacher,
                            name = newProfile.teacher.name,
                            designation = newProfile.teacher.designation,
                            department = newProfile.teacher.department,
                            blood = newProfile.teacher.blood.orEmpty(),
                            facultyId = newProfile.teacher.facultyId,
                        )
                    }
                    ProfileEditMode.CONNECT -> {
                        teacherRepo.changeConnectInfo(
                            teacher = oldProfile.teacher,
                            address = newProfile.teacher.address.orEmpty(),
                            phone = newProfile.teacher.phone.orEmpty(),
                            email = newProfile.teacher.email.orEmpty(),
                            linkedIn = newProfile.teacher.linkedIn.orEmpty(),
                            fbLink = newProfile.teacher.fbLink.orEmpty(),
                        )
                    }
                }
            }.onSuccess {
                updateScreenState(
                    TeacherProfileEditScreenStateUpdate.UpdateMessageState(
                        TeacherProfileEditScreenState.DisplayState.MessageState.Success(
                            "Profile updated successfully!",
                        ),
                    ),
                )
                loadProfile()
            }.onFailure {
                val message = it.message ?: "Failed to update. Please try again."
                updateScreenState(
                    TeacherProfileEditScreenStateUpdate.UpdateMessageState(
                        TeacherProfileEditScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }

    private fun updateProfileScreenState() {
        val profile = newProfileCache ?: return
        val state = when (mode) {
            ProfileEditMode.ACADEMIC -> {
                TeacherProfileEditScreenStateUpdate.ProfileLoadedForAcademic(
                    profile, academicValidationError
                )
            }
            ProfileEditMode.CONNECT -> {
                TeacherProfileEditScreenStateUpdate.ProfileLoadedForConnect(
                    profile, connectValidationError
                )
            }
        }
        updateScreenState(state)
    }

    private fun TeacherProfile.validateAcademic() = TeacherAcademicInfoInputError.INITIAL.copy(
        name = if (teacher.name.isEmpty()) "*Required" else "",
        designation = if (teacher.designation.isEmpty()) "*Required" else "",
        department = if (teacher.department.isEmpty()) "*Required" else "",
    )

    private fun TeacherProfile.validateConnect() = TeacherConnectInfoInputError.INITIAL.copy(
        email = if (teacher.email.isNullOrEmpty()) {
            "*Required"
        } else if (teacher.email?.isValidEmail() == false) {
            "*Invalid email"
        } else {
            ""
        },
    )
}