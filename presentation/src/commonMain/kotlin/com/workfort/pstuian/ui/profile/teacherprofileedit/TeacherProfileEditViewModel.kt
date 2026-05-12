package com.workfort.pstuian.ui.profile.teacherprofileedit

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.TeacherAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.TeacherConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.TeacherRepository
import com.workfort.pstuian.featuredomain.usecase.GetTeacherProfileUserUseCase
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.profile.teacherprofileedit.state.TeacherProfileEditMessageState
import com.workfort.pstuian.ui.profile.teacherprofileedit.state.TeacherProfileEditNavigationState
import com.workfort.pstuian.ui.profile.teacherprofileedit.state.TeacherProfileEditUiEvent
import com.workfort.pstuian.ui.profile.teacherprofileedit.state.TeacherProfileEditUiState
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TeacherProfileEditViewModel(
    private val userId: Int,
    private val teacherRepository: TeacherRepository,
    private val facultyRepository: FacultyRepository,
    private val getTeacherProfileUserUseCase: GetTeacherProfileUserUseCase,
    private val stateMachine: TeacherProfileEditUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<TeacherProfileEditUiState>(stateMachine) {

    private val _message = MutableStateFlow<TeacherProfileEditMessageState?>(null)
    val message: StateFlow<TeacherProfileEditMessageState?> = _message

    private val _navigation = MutableStateFlow<TeacherProfileEditNavigationState?>(null)
    val navigation: StateFlow<TeacherProfileEditNavigationState?> = _navigation

    private var currentProfileCache: UserProfile.TeacherProfile? = null
    private var newProfileCache: UserProfile.TeacherProfile? = null

    override fun onUiReady() {
        loadProfile()
    }

    fun onUiEvent(event: TeacherProfileEditUiEvent) {
        viewModelScope.launch {
            when (event) {
                is TeacherProfileEditUiEvent.BackClicked ->
                    _navigation.update { TeacherProfileEditNavigationState.GoBack }
                is TeacherProfileEditUiEvent.TabClicked -> stateMachine.updateTabIndex(event.index)
                is TeacherProfileEditUiEvent.ProfileInfoChanged -> {
                    newProfileCache = event.profile
                    stateMachine.updateProfileContent(event.profile)
                }
                is TeacherProfileEditUiEvent.FacultySelectionClicked -> newProfileCache?.let {
                    selectFaculty(selectedFacultyId = it.user.facultyId)
                }
                is TeacherProfileEditUiEvent.AcademicInfoSaveClicked -> updateAcademicInfo()
                is TeacherProfileEditUiEvent.ConnectInfoSaveClicked -> updateConnectInfo()
            }
        }
    }

    private fun loadProfile() {
        _message.update { TeacherProfileEditMessageState.Loading() }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            getTeacherProfileUserUseCase(userId).onSuccess { profile ->
                onMessageHandled()
                if (!profile.isSignedIn) {
                    _message.update { TeacherProfileEditMessageState.Error("Sign in required") }
                    _navigation.update { TeacherProfileEditNavigationState.GoBack }
                    return@launchOnMain
                }
                currentProfileCache = profile
                newProfileCache = profile
                stateMachine.updateProfileContent(profile)
            }.onFailure {
                val message = it.message ?: "Failed to load profile"
                _message.update { TeacherProfileEditMessageState.Error(message) }
            }
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun selectFaculty(selectedFacultyId: Int) {
        val profile = newProfileCache ?: return
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            stateMachine.showLoading(true)
            val result = facultyRepository.getFaculties()
            stateMachine.showLoading(false)

            val faculties = result.getOrNull()
            if (faculties.isNullOrEmpty()) {
                val message = result.exceptionOrNull()?.message ?: "Failed to load faculties. Please try again later."
                _message.update { TeacherProfileEditMessageState.Error(message) }
                return@launchOnMain
            }

            _message.update {
                TeacherProfileEditMessageState.FacultySelection(
                    faculties = faculties,
                    selectedFacultyId = selectedFacultyId,
                    onSaveAndContinue = { faculty ->
                        onMessageHandled()
                        faculty?.let { faculty ->
                            newProfileCache = profile.copy(
                                user = profile.user.copy(facultyId = faculty.id),
                                faculty = faculty,
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
            _message.update { TeacherProfileEditMessageState.ShowSnackBar("No change") }
            return
        }

        _message.update {
            TeacherProfileEditMessageState.ConfirmSave {
                viewModelScope.launchOnMain(coroutineDispatcherProvider) {
                    stateMachine.showLoading(isLoading = true)
                    teacherRepository.changeAcademicInfo(
                        userId = newProfile.user.userId,
                        name = newProfile.user.name,
                        designation = newProfile.user.designation,
                        department = newProfile.user.department,
                        blood = newProfile.user.blood.orEmpty(),
                        facultyId = newProfile.user.facultyId,
                    ).onSuccess {
                        stateMachine.showLoading(isLoading = false)
                        _message.update { TeacherProfileEditMessageState.ShowSnackBar("Updated successfully") }
                    }.onFailure {
                        stateMachine.showLoading(isLoading = false)
                        val message = it.message ?: "Failed to update. Please try again."
                        _message.update { TeacherProfileEditMessageState.Error(message) }
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
            _message.update { TeacherProfileEditMessageState.ShowSnackBar("No change") }
            return
        }

        _message.update {
            TeacherProfileEditMessageState.ConfirmSave {
                viewModelScope.launchOnMain(coroutineDispatcherProvider) {
                    stateMachine.showLoading(isLoading = true)
                    teacherRepository.changeConnectInfo(
                        userId = newProfile.user.userId,
                        address = newProfile.user.address.orEmpty(),
                        phone = newProfile.user.phone.orEmpty(),
                        oldEmail = currentProfile.user.email,
                        email = newProfile.user.email,
                        linkedIn = newProfile.user.linkedIn.orEmpty(),
                        fbLink = newProfile.user.fbLink.orEmpty(),
                    ).onSuccess {
                        stateMachine.showLoading(isLoading = false)
                        _message.update { TeacherProfileEditMessageState.ShowSnackBar("Updated successfully") }
                    }.onFailure {
                        stateMachine.showLoading(isLoading = false)
                        val message = it.message ?: "Failed to update. Please try again."
                        _message.update { TeacherProfileEditMessageState.Error(message) }
                    }
                }
            }
        }
    }

    fun validateAcademic(profile: UserProfile.TeacherProfile) = TeacherAcademicInfoInputError.INITIAL.copy(
        name = if (profile.user.name.isEmpty()) "*Required" else "",
        designation = if (profile.user.designation.isEmpty()) "*Required" else "",
        department = if (profile.user.department.isEmpty()) "*Required" else "",
    )

    fun validateConnect(profile: UserProfile.TeacherProfile) = TeacherConnectInfoInputError.INITIAL.copy(
        email = if (profile.user.email.isEmpty()) {
            "*Required"
        } else if (!profile.user.email.isValidEmail()) {
            "*Invalid email"
        } else {
            ""
        },
    )
}
