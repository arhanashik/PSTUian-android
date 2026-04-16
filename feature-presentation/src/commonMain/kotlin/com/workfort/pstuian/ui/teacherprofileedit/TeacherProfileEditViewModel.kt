package com.workfort.pstuian.ui.teacherprofileedit

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.TeacherAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.TeacherConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.TeacherProfile
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.TeacherRepository
import com.workfort.pstuian.ui.teacherprofileedit.state.TeacherProfileEditMessageState
import com.workfort.pstuian.ui.teacherprofileedit.state.TeacherProfileEditNavigationState
import com.workfort.pstuian.ui.teacherprofileedit.state.TeacherProfileEditUiEvent
import com.workfort.pstuian.ui.teacherprofileedit.state.TeacherProfileEditUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TeacherProfileEditViewModel(
    private val userId: Int,
    private val mode: ProfileEditMode,
    private val teacherRepo: TeacherRepository,
    private val facultyRepo: FacultyRepository,
    private val stateMachine: TeacherProfileEditUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<TeacherProfileEditUiState>(stateMachine) {

    private val _message = MutableStateFlow<TeacherProfileEditMessageState?>(null)
    val message: StateFlow<TeacherProfileEditMessageState?> = _message

    private val _navigation = MutableStateFlow<TeacherProfileEditNavigationState?>(null)
    val navigation: StateFlow<TeacherProfileEditNavigationState?> = _navigation

    private var oldProfileCache: TeacherProfile? = null
    private var newProfileCache: TeacherProfile? = null
    private var academicValidationError = TeacherAcademicInfoInputError.INITIAL
    private var connectValidationError = TeacherConnectInfoInputError.INITIAL

    override fun onUiReady() {
        stateMachine.setInitialContent()
        onUiEvent(TeacherProfileEditUiEvent.LoadProfile)
    }

    fun onUiEvent(event: TeacherProfileEditUiEvent) {
        viewModelScope.launch {
            when (event) {
                is TeacherProfileEditUiEvent.LoadProfile -> loadProfile()
                is TeacherProfileEditUiEvent.ChangeProfile -> onChangeProfile(event.profile)
                is TeacherProfileEditUiEvent.ClickBack -> onClickBack()
                is TeacherProfileEditUiEvent.ClickSave -> onClickSave()
                is TeacherProfileEditUiEvent.ClickFaculty -> onClickFaculty()
                is TeacherProfileEditUiEvent.ChangeFaculty -> onChangeFaculty(event.facultyId)
                is TeacherProfileEditUiEvent.MessageConsumed -> onMessageHandled()
                is TeacherProfileEditUiEvent.NavigationConsumed -> onNavigationHandled()
            }
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        _navigation.update { TeacherProfileEditNavigationState.GoBack }
    }

    private fun onClickSave() {
        _message.update {
            TeacherProfileEditMessageState.ConfirmSave {
                updateProfile()
            }
        }
    }

    private fun onClickFaculty() = newProfileCache?.let { profile ->
        _navigation.update {
            TeacherProfileEditNavigationState.GoToFacultyPickerScreen(
                mode = com.workfort.pstuian.featuredomain.model.FacultySelectionMode.FACULTY,
                facultyId = profile.teacher.facultyId,
            )
        }
    }

    private suspend fun onChangeFaculty(facultyId: Int) {
        if (newProfileCache?.faculty?.id == facultyId) return
        _message.update { TeacherProfileEditMessageState.Loading(cancelable = false) }
        runCatching {
            val faculty = facultyRepo.getFaculty(facultyId)
            newProfileCache?.let {
                val newProfile = it.copy(
                    teacher = it.teacher.copy(facultyId = faculty.id),
                    faculty = faculty,
                )
                newProfileCache = newProfile
                onMessageHandled()
                updateProfileScreenState()
            }
        }.onFailure {
            val message = it.message ?: "Failed to load faculty"
            _message.update { TeacherProfileEditMessageState.Error(message) }
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
        stateMachine.updatePanelState(TeacherProfileEditUiState.PanelState.Loading)
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
            stateMachine.updatePanelState(TeacherProfileEditUiState.PanelState.Error(message))
        }
    }

    private fun updateProfile() {
        if (academicValidationError.isNotEmpty() || connectValidationError.isNotEmpty()) {
            _message.update {
                TeacherProfileEditMessageState.Error(
                    "Please insert required fields and try again!"
                )
            }
            return
        }
        _message.update { TeacherProfileEditMessageState.Loading(cancelable = false) }
        val oldProfile = oldProfileCache ?: return
        val newProfile = newProfileCache ?: return

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
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
                _message.update {
                    TeacherProfileEditMessageState.Success(
                        "Profile updated successfully!"
                    )
                }
                loadProfile()
            }.onFailure {
                val message = it.message ?: "Failed to update. Please try again."
                _message.update { TeacherProfileEditMessageState.Error(message) }
            }
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
