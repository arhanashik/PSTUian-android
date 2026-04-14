package com.workfort.pstuian.ui.teacherprofileedit

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.TeacherAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.TeacherConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.TeacherProfile
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.TeacherRepository
import com.workfort.pstuian.ui.teacherprofileedit.state.TeacherProfileEditUiEvent
import com.workfort.pstuian.ui.teacherprofileedit.state.TeacherProfileEditUiState
import kotlinx.coroutines.launch

class TeacherProfileEditViewModel(
    private val userId: Int,
    private val mode: ProfileEditMode,
    private val teacherRepo: TeacherRepository,
    private val facultyRepo: FacultyRepository,
    private val stateMachine: TeacherProfileEditUiStateMachine,
) : UiStateMachineViewModel<TeacherProfileEditUiState>(stateMachine) {

    private var oldProfileCache: TeacherProfile? = null
    private var newProfileCache: TeacherProfile? = null
    private var academicValidationError = TeacherAcademicInfoInputError.INITIAL
    private var connectValidationError = TeacherConnectInfoInputError.INITIAL

    override fun onUiReady() {
        onUiEvent(TeacherProfileEditUiEvent.LoadProfile)
    }

    fun onUiEvent(event: TeacherProfileEditUiEvent) {
        viewModelScope.launch {
            when (event) {
                is TeacherProfileEditUiEvent.LoadProfile -> loadProfile()
                is TeacherProfileEditUiEvent.ChangeProfile -> onChangeProfile(event.profile)
                is TeacherProfileEditUiEvent.ClickBack -> stateMachine.onClickBack()
                is TeacherProfileEditUiEvent.ClickSave -> stateMachine.onClickSave()
                is TeacherProfileEditUiEvent.ClickFaculty -> onClickFaculty()
                is TeacherProfileEditUiEvent.ChangeFaculty -> onChangeFaculty(event.facultyId)
                is TeacherProfileEditUiEvent.Save -> updateProfile()
                is TeacherProfileEditUiEvent.MessageConsumed -> stateMachine.messageConsumed()
                is TeacherProfileEditUiEvent.NavigationConsumed -> stateMachine.navigationConsumed()
            }
        }
    }

    private fun onClickFaculty() = newProfileCache?.let {
        stateMachine.onClickFaculty(it)
    }

    private suspend fun onChangeFaculty(facultyId: Int) {
        if (newProfileCache?.faculty?.id == facultyId) return
        stateMachine.updateMessageState(TeacherProfileEditUiState.DisplayState.MessageState.Loading(cancelable = false))
        runCatching {
            val faculty = facultyRepo.getFaculty(facultyId)
            newProfileCache?.let {
                val newProfile = it.copy(
                    teacher = it.teacher.copy(facultyId = faculty.id),
                    faculty = faculty,
                )
                newProfileCache = newProfile
                stateMachine.messageConsumed()
                updateProfileScreenState()
            }
        }.onFailure {
            val message = it.message ?: "Failed to load faculty"
            stateMachine.updateMessageState(TeacherProfileEditUiState.DisplayState.MessageState.Error(message))
        }
    }

    private fun onChangeProfile(profile: TeacherProfile) {
        newProfileCache = profile
        when (mode) {
            ProfileEditMode.ACADEMIC -> academicValidationError = stateMachine.validateAcademic(profile)
            ProfileEditMode.CONNECT -> connectValidationError = stateMachine.validateConnect(profile)
        }
        updateProfileScreenState()
    }

    private suspend fun loadProfile() {
        stateMachine.updatePanelState(TeacherProfileEditUiState.DisplayState.PanelState.Loading)
        runCatching {
            oldProfileCache = teacherRepo.getProfile(userId)
            newProfileCache = oldProfileCache
            when (mode) {
                ProfileEditMode.ACADEMIC -> {
                    academicValidationError = newProfileCache?.let { stateMachine.validateAcademic(it) }
                        ?: TeacherAcademicInfoInputError.INITIAL
                }
                ProfileEditMode.CONNECT -> {
                    connectValidationError = newProfileCache?.let { stateMachine.validateConnect(it) }
                        ?: TeacherConnectInfoInputError.INITIAL
                }
            }
            updateProfileScreenState()
        }.onFailure {
            val message = it.message ?: "Failed to load profile"
            stateMachine.updatePanelState(TeacherProfileEditUiState.DisplayState.PanelState.Error(message))
        }
    }

    private suspend fun updateProfile() {
        if (academicValidationError.isNotEmpty() || connectValidationError.isNotEmpty()) {
            stateMachine.updateMessageState(
                TeacherProfileEditUiState.DisplayState.MessageState.Error(
                    "Please insert required fields and try again!"
                )
            )
            return
        }
        stateMachine.updateMessageState(TeacherProfileEditUiState.DisplayState.MessageState.Loading(cancelable = false))
        val oldProfile = oldProfileCache ?: return
        val newProfile = newProfileCache ?: return
        runCatching {
            when (mode) {
                ProfileEditMode.ACADEMIC -> {
                    teacherRepo.changeAcademicInfo(
                        teacher = oldProfile.teacher.toEntity(),
                        name = newProfile.teacher.name,
                        designation = newProfile.teacher.designation,
                        department = newProfile.teacher.department,
                        blood = newProfile.teacher.blood.orEmpty(),
                        facultyId = newProfile.teacher.facultyId,
                    )
                }
                ProfileEditMode.CONNECT -> {
                    teacherRepo.changeConnectInfo(
                        teacher = oldProfile.teacher.toEntity(),
                        address = newProfile.teacher.address.orEmpty(),
                        phone = newProfile.teacher.phone.orEmpty(),
                        email = newProfile.teacher.email.orEmpty(),
                        linkedIn = newProfile.teacher.linkedIn.orEmpty(),
                        fbLink = newProfile.teacher.fbLink.orEmpty(),
                    )
                }
            }
        }.onSuccess {
            stateMachine.updateMessageState(
                TeacherProfileEditUiState.DisplayState.MessageState.Success(
                    "Profile updated successfully!"
                )
            )
            loadProfile()
        }.onFailure {
            val message = it.message ?: "Failed to update. Please try again."
            stateMachine.updateMessageState(TeacherProfileEditUiState.DisplayState.MessageState.Error(message))
        }
    }

    private fun updateProfileScreenState() {
        val profile = newProfileCache ?: return
        stateMachine.updateProfileScreenState(
            profile,
            academicValidationError,
            connectValidationError,
            mode
        )
    }
}
