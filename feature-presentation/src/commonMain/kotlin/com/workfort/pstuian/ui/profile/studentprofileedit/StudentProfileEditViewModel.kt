package com.workfort.pstuian.ui.profile.studentprofileedit

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
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
    private val facultyRepo: FacultyRepository,
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
                is StudentProfileEditUiEvent.FacultySelectionClicked -> onClickFaculty()
                is StudentProfileEditUiEvent.BatchSelectionClicked -> onClickBatch()
                is StudentProfileEditUiEvent.AcademicInfoSaveClicked -> {
                    if (newProfileCache == null || currentProfileCache == newProfileCache) {
                        _message.update { StudentProfileEditMessageState.ShowSnackBar("No change") }
                    } else {
                        _message.update { StudentProfileEditMessageState.ConfirmSave(::updateAcademicInfo) }
                    }
                }
                is StudentProfileEditUiEvent.ConnectInfoSaveClicked -> {
                    if (newProfileCache == null || currentProfileCache == newProfileCache) {
                        _message.update { StudentProfileEditMessageState.ShowSnackBar("No change") }
                    } else {
                        _message.update { StudentProfileEditMessageState.ConfirmSave(::updateConnectInfo) }
                    }
                }
            }
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickFaculty() = newProfileCache?.let { profile ->
        // TODO show faculty picker bottom sheet
    }

    private fun onClickBatch() = newProfileCache?.let { profile ->
        // TODO show batch picker bottom sheet
    }

    private fun loadProfile() {
        _message.update { StudentProfileEditMessageState.Loading() }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            getStudentProfileUserUseCase(userId)
                .onSuccess { profile ->
                    onMessageHandled()
                    currentProfileCache = profile
                    stateMachine.updateProfileContent(profile)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load profile"
                    _message.update { StudentProfileEditMessageState.Error(message) }
                }
        }
    }

    private fun updateAcademicInfo() {
        val currentProfile = currentProfileCache ?: return
        val newProfile = newProfileCache ?: return

        val validationResult = validateAcademic(newProfile)
        stateMachine.updateAcademicInfoInputError(validationResult)

        if (validationResult.hasError()) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            stateMachine.showLoading(isLoading = true)
            studentRepo.changeAcademicInfo(
                userId = currentProfile.student.userId,
                name = newProfile.student.name,
                studentId = newProfile.student.studentId,
                reg = newProfile.student.reg,
                blood = newProfile.student.blood.orEmpty(),
                facultyId = newProfile.student.facultyId,
                session = newProfile.student.session,
                batchId = newProfile.student.batchId,
            )
                .onSuccess {
                    stateMachine.showLoading(isLoading = false)
                    _message.update { StudentProfileEditMessageState.ShowSnackBar("Updated successfully") }
                }
                .onFailure {
                    stateMachine.showLoading(isLoading = false)
                    val message = it.message ?: "Failed to update. Please try again."
                    _message.update { StudentProfileEditMessageState.Error(message) }
                }
        }
    }

    private fun updateConnectInfo() {
        val currentProfile = currentProfileCache ?: return
        val newProfile = newProfileCache

        if (newProfile == null || currentProfile == newProfile) {
            StudentProfileEditMessageState.ShowSnackBar("No change")
            return
        }

        val validationResult = validateConnect(newProfile)
        stateMachine.updateConnectInfoInputError(validationResult)

        if (validationResult.hasError()) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            stateMachine.showLoading(isLoading = true)
            studentRepo.changeConnectInfo(
                userId = currentProfile.student.userId,
                address = newProfile.student.address.orEmpty(),
                phone = newProfile.student.phone.orEmpty(),
                oldEmail = currentProfile.student.email,
                newEmail = newProfile.student.email,
                cvLink = newProfile.student.cvLink.orEmpty(),
                linkedIn = newProfile.student.linkedIn.orEmpty(),
                facebook = newProfile.student.fbLink.orEmpty(),
            )
                .onSuccess {
                    stateMachine.showLoading(isLoading = false)
                    _message.update { StudentProfileEditMessageState.ShowSnackBar("Updated successfully") }
                }
                .onFailure {
                    stateMachine.showLoading(isLoading = false)
                    val message = it.message ?: "Failed to update. Please try again."
                    _message.update { StudentProfileEditMessageState.Error(message) }
                }
        }
    }

    fun validateAcademic(profile: UserProfile.StudentProfile) = StudentAcademicInfoInputError.INITIAL.copy(
        name = if (profile.student.name.isEmpty()) "*Required" else "",
        id = if (profile.student.studentId == 0) "*Required" else "",
        reg = if (profile.student.reg.isEmpty()) "*Required" else "",
        session = if (profile.student.session.isEmpty()) "*Required" else "",
    )

    fun validateConnect(profile: UserProfile.StudentProfile) = StudentConnectInfoInputError.INITIAL.copy(
        email = if (profile.student.email.isEmpty()) {
            "*Required"
        } else if (!profile.student.email.isValidEmail()) {
            "*Invalid email"
        } else {
            ""
        },
    )
}
