package com.workfort.pstuian.ui.profile.studentprofileedit

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.Faculty
import com.workfort.pstuian.featuredomain.model.StudentAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.StudentConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.StudentRepository
import com.workfort.pstuian.featuredomain.usecase.GetStudentProfileUserUseCase
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditMessageState
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditNavigationState
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditUiEvent
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditUiState
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StudentProfileEditViewModel(
    private val userId: Int,
    private val studentRepo: StudentRepository,
    private val facultyRepository: FacultyRepository,
    private val getStudentProfileUserUseCase: GetStudentProfileUserUseCase,
    private val stateMachine: StudentProfileEditUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<StudentProfileEditUiState>(stateMachine) {

    private val _message = MutableStateFlow<StudentProfileEditMessageState?>(null)
    val message: StateFlow<StudentProfileEditMessageState?> = _message

    private val _navigation = MutableStateFlow<StudentProfileEditNavigationState?>(null)
    val navigation: StateFlow<StudentProfileEditNavigationState?> = _navigation

    private var currentProfileCache: UserProfile.StudentProfile? = null
    private var newProfileCache: UserProfile.StudentProfile? = null

    override fun onUiReady() {
        loadProfile()
    }

    fun onUiEvent(event: StudentProfileEditUiEvent) {
        viewModelScope.launch {
            when (event) {
                is StudentProfileEditUiEvent.BackClicked ->
                    _navigation.update { StudentProfileEditNavigationState.GoBack }
                is StudentProfileEditUiEvent.TabClicked -> stateMachine.updateTabIndex(event.index)
                is StudentProfileEditUiEvent.ProfileInfoChanged -> {
                    newProfileCache = event.profile
                    stateMachine.updateProfileContent(event.profile)
                }
                is StudentProfileEditUiEvent.FacultySelectionClicked -> newProfileCache?.let {
                    selectFaculty(selectedFacultyId = it.user.facultyId, needFacultySelection = true)
                }
                is StudentProfileEditUiEvent.BatchSelectionClicked -> newProfileCache?.let {
                    selectFaculty(it.user.facultyId, needFacultySelection = false)
                }
                is StudentProfileEditUiEvent.AcademicInfoSaveClicked -> updateAcademicInfo()
                is StudentProfileEditUiEvent.ConnectInfoSaveClicked -> updateConnectInfo()
            }
        }
    }

    private fun loadProfile() {
        _message.update { StudentProfileEditMessageState.Loading() }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            getStudentProfileUserUseCase(userId).onSuccess { profile ->
                onMessageHandled()
                if (!profile.isSignedIn) {
                    _message.update { StudentProfileEditMessageState.Error("Sign in required") }
                    _navigation.update { StudentProfileEditNavigationState.GoBack }
                    return@launchOnMain
                }
                currentProfileCache = profile
                newProfileCache = profile
                stateMachine.updateProfileContent(profile)
            }.onFailure {
                val message = it.message ?: "Failed to load profile"
                _message.update { StudentProfileEditMessageState.Error(message) }
            }
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun selectFaculty(selectedFacultyId: Int, needFacultySelection: Boolean) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            stateMachine.showLoading(true)
            val result = facultyRepository.getFaculties()
            stateMachine.showLoading(false)

            val faculties = result.getOrNull()
            if (faculties.isNullOrEmpty()) {
                val message = result.exceptionOrNull()?.message ?: "Failed to load faculties. Please try again later."
                _message.update { StudentProfileEditMessageState.Error(message) }
                return@launchOnMain
            }

            if (!needFacultySelection) {
                faculties.firstOrNull { it.id == selectedFacultyId }?.let { selectBatch(it) }
                return@launchOnMain
            }

            _message.update {
                StudentProfileEditMessageState.FacultySelection(
                    faculties = faculties,
                    selectedFacultyId = selectedFacultyId,
                    onSaveAndContinue = { faculty ->
                        onMessageHandled()
                        faculty?.let { selectBatch(it) }
                    },
                )
            }
        }
    }

    private fun selectBatch(faculty: Faculty) {
        val profile = newProfileCache ?: return
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            stateMachine.showLoading(true)
            val result = facultyRepository.getBatches(faculty.id)
            stateMachine.showLoading(false)

            val batches = result.getOrNull()
            if (batches.isNullOrEmpty()) {
                val message = result.exceptionOrNull()?.message ?: "Failed to load batches. Please try again later."
                _message.update { StudentProfileEditMessageState.Error(message) }
                return@launchOnMain
            }

            _message.update {
                StudentProfileEditMessageState.BatchSelection(
                    batches = batches,
                    selectedBatchId = profile.user.batchId,
                    onSaveAndContinue = { batch ->
                        onMessageHandled()
                        batch?.let { batch ->
                            newProfileCache = profile.copy(
                                user = profile.user.copy(facultyId = faculty.id, batchId = batch.id),
                                faculty = faculty,
                                batch = batch,
                            )
                            newProfileCache?.let { stateMachine.updateProfileContent(it) }
                        }
                    },
                )
            }
        }
    }

    private fun updateAcademicInfo() {
        val currentProfile = currentProfileCache ?: return
        val newProfile = newProfileCache ?: return
        val validationResult = validateAcademic(newProfile)

        stateMachine.updateAcademicInfoInputError(validationResult)
        if (validationResult.hasError()) return

        if (currentProfile == newProfile) {
            _message.update { StudentProfileEditMessageState.ShowSnackBar("No change") }
            return
        }

        _message.update {
            StudentProfileEditMessageState.ConfirmSave {
                viewModelScope.launchOnMain(coroutineDispatcherProvider) {
                    stateMachine.showLoading(isLoading = true)
                    studentRepo.changeAcademicInfo(
                        name = newProfile.user.name,
                        studentOldId = currentProfile.user.userId,
                        studentId = newProfile.user.userId,
                        reg = newProfile.user.reg,
                        blood = newProfile.user.blood.orEmpty(),
                        facultyId = newProfile.user.facultyId,
                        session = newProfile.user.session,
                        batchId = newProfile.user.batchId,
                    ).onSuccess {
                        stateMachine.showLoading(isLoading = false)
                        _message.update { StudentProfileEditMessageState.ShowSnackBar("Updated successfully") }
                    }.onFailure {
                        stateMachine.showLoading(isLoading = false)
                        val message = it.message ?: "Failed to update. Please try again."
                        _message.update { StudentProfileEditMessageState.Error(message) }
                    }
                }
            }
        }
    }

    private fun updateConnectInfo() {
        val currentProfile = currentProfileCache ?: return
        val newProfile = newProfileCache ?: return
        val validationResult = validateConnect(newProfile)

        stateMachine.updateConnectInfoInputError(validationResult)
        if (validationResult.hasError()) return

        if (currentProfile == newProfile) {
            _message.update { StudentProfileEditMessageState.ShowSnackBar("No change") }
            return
        }

        _message.update {
            StudentProfileEditMessageState.ConfirmSave {
                viewModelScope.launchOnMain(coroutineDispatcherProvider) {
                    stateMachine.showLoading(isLoading = true)
                    studentRepo.changeConnectInfo(
                        userId = currentProfile.user.userId,
                        address = newProfile.user.address.orEmpty(),
                        phone = newProfile.user.phone.orEmpty(),
                        oldEmail = currentProfile.user.email,
                        newEmail = newProfile.user.email,
                        cvLink = newProfile.user.cvLink.orEmpty(),
                        linkedIn = newProfile.user.linkedIn.orEmpty(),
                        facebook = newProfile.user.fbLink.orEmpty(),
                    ).onSuccess {
                        stateMachine.showLoading(isLoading = false)
                        _message.update { StudentProfileEditMessageState.ShowSnackBar("Updated successfully") }
                    }.onFailure {
                        stateMachine.showLoading(isLoading = false)
                        val message = it.message ?: "Failed to update. Please try again."
                        _message.update { StudentProfileEditMessageState.Error(message) }
                    }
                }
            }
        }
    }

    fun validateAcademic(profile: UserProfile.StudentProfile) = StudentAcademicInfoInputError.INITIAL.copy(
        name = if (profile.user.name.isEmpty()) "*Required" else "",
        id = if (profile.user.userId == 0) "*Required" else "",
        reg = if (profile.user.reg.isEmpty()) "*Required" else "",
        session = if (profile.user.session.isEmpty()) "*Required" else "",
    )

    fun validateConnect(profile: UserProfile.StudentProfile) = StudentConnectInfoInputError.INITIAL.copy(
        email = if (profile.user.email.isEmpty()) {
            "*Required"
        } else if (!profile.user.email.isValidEmail()) {
            "*Invalid email"
        } else {
            ""
        },
    )
}
