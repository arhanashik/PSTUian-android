package com.workfort.pstuian.ui.studentprofileedit

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.StudentAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.StudentConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.StudentProfile
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.StudentRepository
import com.workfort.pstuian.ui.studentprofileedit.state.StudentProfileEditMessageState
import com.workfort.pstuian.ui.studentprofileedit.state.StudentProfileEditNavigationState
import com.workfort.pstuian.ui.studentprofileedit.state.StudentProfileEditUiEvent
import com.workfort.pstuian.ui.studentprofileedit.state.StudentProfileEditUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StudentProfileEditViewModel(
    private val userId: Int,
    private val mode: ProfileEditMode,
    private val studentRepo: StudentRepository,
    private val facultyRepo: FacultyRepository,
    private val stateMachine: StudentProfileEditUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<StudentProfileEditUiState>(stateMachine) {

    private val _message = MutableStateFlow<StudentProfileEditMessageState?>(null)
    val message: StateFlow<StudentProfileEditMessageState?> = _message

    private val _navigation = MutableStateFlow<StudentProfileEditNavigationState?>(null)
    val navigation: StateFlow<StudentProfileEditNavigationState?> = _navigation

    private var oldProfileCache: StudentProfile? = null
    private var newProfileCache: StudentProfile? = null
    private var academicValidationError = StudentAcademicInfoInputError.INITIAL
    private var connectValidationError = StudentConnectInfoInputError.INITIAL

    override fun onUiReady() {
        stateMachine.setInitialContent()
        onUiEvent(StudentProfileEditUiEvent.LoadProfile)
    }

    fun onUiEvent(event: StudentProfileEditUiEvent) {
        viewModelScope.launch {
            when (event) {
                is StudentProfileEditUiEvent.LoadProfile -> loadProfile()
                is StudentProfileEditUiEvent.ChangeProfile -> onChangeProfile(event.profile)
                is StudentProfileEditUiEvent.ClickBack -> onClickBack()
                is StudentProfileEditUiEvent.ClickSave -> onClickSave()
                is StudentProfileEditUiEvent.ClickFaculty -> onClickFaculty()
                is StudentProfileEditUiEvent.ClickBatch -> onClickBatch()
                is StudentProfileEditUiEvent.ChangeFaculty -> onChangeFaculty(event.facultyId)
                is StudentProfileEditUiEvent.ChangeBatch -> onChangeBatch(event.batchId)
                is StudentProfileEditUiEvent.Save -> updateProfile()
            }
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        _navigation.update { StudentProfileEditNavigationState.GoBack }
    }

    private fun onClickSave() {
        _message.update {
            StudentProfileEditMessageState.ConfirmSave {
                updateProfile()
            }
        }
    }

    private fun onClickFaculty() = newProfileCache?.let { profile ->
        _navigation.update {
            StudentProfileEditNavigationState.GoToFacultyPickerScreen(
                mode = com.workfort.pstuian.featuredomain.model.FacultySelectionMode.BOTH,
                facultyId = profile.student.facultyId,
                batchId = profile.student.batchId,
            )
        }
    }

    private fun onClickBatch() = newProfileCache?.let { profile ->
        _navigation.update {
            StudentProfileEditNavigationState.GoToFacultyPickerScreen(
                mode = com.workfort.pstuian.featuredomain.model.FacultySelectionMode.BATCH,
                facultyId = profile.student.facultyId,
                batchId = profile.student.batchId,
            )
        }
    }

    private suspend fun onChangeFaculty(facultyId: Int) {
        if (newProfileCache?.faculty?.id == facultyId) return
        _message.update { StudentProfileEditMessageState.Loading(cancelable = false) }
        runCatching {
            val faculty = facultyRepo.getFaculty(facultyId)
            newProfileCache?.let {
                val newProfile = it.copy(
                    student = it.student.copy(facultyId = faculty.id),
                    faculty = faculty,
                )
                newProfileCache = newProfile
                onMessageHandled()
                updateProfileScreenState()
            }
        }.onFailure {
            val message = it.message ?: "Failed to load faculty"
            _message.update { StudentProfileEditMessageState.Error(message) }
        }
    }

    private suspend fun onChangeBatch(batchId: Int) {
        if (newProfileCache?.batch?.id == batchId) return
        _message.update { StudentProfileEditMessageState.Loading(cancelable = false) }
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
                onMessageHandled()
                updateProfileScreenState()
            }
        }.onFailure {
            val message = it.message ?: "Failed to load batch"
            _message.update { StudentProfileEditMessageState.Error(message) }
        }
    }

    private fun onChangeProfile(profile: StudentProfile) {
        newProfileCache = profile
        when (mode) {
            ProfileEditMode.ACADEMIC -> academicValidationError = stateMachine.validateAcademic(profile)
            ProfileEditMode.CONNECT -> connectValidationError = stateMachine.validateConnect(profile)
        }
        updateProfileScreenState()
    }

    private suspend fun loadProfile() {
        stateMachine.updatePanelState(StudentProfileEditUiState.PanelState.Loading)
        runCatching {
            oldProfileCache = studentRepo.getProfile(userId)
            newProfileCache = oldProfileCache
            when (mode) {
                ProfileEditMode.ACADEMIC -> {
                    academicValidationError = newProfileCache?.let { stateMachine.validateAcademic(it) }
                        ?: StudentAcademicInfoInputError.INITIAL
                }
                ProfileEditMode.CONNECT -> {
                    connectValidationError = newProfileCache?.let { stateMachine.validateConnect(it) }
                        ?: StudentConnectInfoInputError.INITIAL
                }
            }
            updateProfileScreenState()
        }.onFailure {
            val message = it.message ?: "Failed to load profile"
            stateMachine.updatePanelState(StudentProfileEditUiState.PanelState.Error(message))
        }
    }

    private fun updateProfile() {
        if (academicValidationError.isNotEmpty() || connectValidationError.isNotEmpty()) {
            _message.update {
                StudentProfileEditMessageState.Error(
                    "Please insert required fields and try again!"
                )
            }
            return
        }
        _message.update { StudentProfileEditMessageState.Loading(cancelable = false) }
        val oldProfile = oldProfileCache ?: return
        val newProfile = newProfileCache ?: return

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
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
                _message.update {
                    StudentProfileEditMessageState.Success(
                        "Profile updated successfully!"
                    )
                }
                loadProfile()
            }.onFailure {
                val message = it.message ?: "Failed to update. Please try again."
                _message.update { StudentProfileEditMessageState.Error(message) }
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
