package com.workfort.pstuian.ui.profile.teacherprofile

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.data.infrastructure.repository.TeacherRepositoryImpl
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.TeacherProfile
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.featuredomain.usecase.GetTeacherProfileUserUseCase
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.profile.common.state.ProfileScreenUiStateMachine
import com.workfort.pstuian.ui.profile.common.state.ProfileUiEvent
import com.workfort.pstuian.ui.profile.common.state.ProfileUiState
import com.workfort.pstuian.ui.profile.teacherprofile.state.TeacherProfileMessageState
import com.workfort.pstuian.ui.profile.teacherprofile.state.TeacherProfileNavigationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TeacherProfileViewModel(
    private val userId: Int,
    private val teacherRepo: TeacherRepositoryImpl,
    private val authRepo: AuthRepository,
    private val settingsRepository: SettingsRepository,
    private val getTeacherProfileUserUseCase: GetTeacherProfileUserUseCase,
    private val displayDataMapper: TeacherProfileDisplayDataMapper,
    private val uiStateMachine: ProfileScreenUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<ProfileUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<TeacherProfileMessageState?>(null)
    val message: StateFlow<TeacherProfileMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<TeacherProfileNavigationState?>(null)
    val navigation: StateFlow<TeacherProfileNavigationState?> = _navigation.asStateFlow()

    private var profileCache: TeacherProfile? = null

    override fun onUiReady() {
        loadProfile()
    }

    fun onUiEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.BackClicked -> _navigation.update { TeacherProfileNavigationState.GoBack }
            is ProfileUiEvent.FollowClicked -> onClickFollow()
            is ProfileUiEvent.ImageClicked -> onClickImage(event.url)
            is ProfileUiEvent.CallClicked -> onClickCall()
            is ProfileUiEvent.EmailClicked -> onClickEmail()
            is ProfileUiEvent.SignOutClicked -> onClickSignOut()
            is ProfileUiEvent.TabClicked -> onClickTab(event.index)
            is ProfileUiEvent.RefreshClicked -> onClickRefresh()
            is ProfileUiEvent.ChangeImageClicked -> onClickChangeImage()
            is ProfileUiEvent.EditBioClicked -> onClickEditBio()
            is ProfileUiEvent.EditClicked -> onClickEdit(event.selectedTabIndex)
            is ProfileUiEvent.MyBloodDonationListClicked -> Unit
            is ProfileUiEvent.ChangePasswordClicked -> onClickChangePassword()
            is ProfileUiEvent.DownloadCvClicked -> Unit
            is ProfileUiEvent.UploadCvClicked -> Unit
            is ProfileUiEvent.MyCheckInListClicked -> Unit
            is ProfileUiEvent.MyDeviceListClicked -> onClickMyDeviceList()
            is ProfileUiEvent.DeleteAccountClicked -> onClickDeleteAccount()
            is ProfileUiEvent.ChangeProfileImage -> changeProfileImage(event.imageUrl)
        }
    }

    fun messageHandled() = _message.update { null }

    fun navigationHandled() = _navigation.update { null }

    private fun loadProfile() {
        uiStateMachine.showProfileLoading()
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            getTeacherProfileUserUseCase(userId)
                .onSuccess { profile ->
                    profileCache = profile
                    uiStateMachine.showProfile(
                        headerDisplayData = displayDataMapper.mapHeaderData(profile),
                        academicContents = displayDataMapper.mapAcademicContents(profile),
                        connectContents = displayDataMapper.mapConnectContents(profile),
                        isSignedIn = profile.isSignedIn,
                    )
                }
                .onFailure {
                    val message = it.message ?: "Failed to load teacher profile"
                    uiStateMachine.showProfileError(message)
                }
        }
    }

    private fun onClickFollow() {
        _message.update { TeacherProfileMessageState.Success("Follow feature will be available soon!") }
    }

    private fun onClickImage(url: String) {
        _navigation.update { TeacherProfileNavigationState.ImagePreviewScreen(url) }
    }

    private fun onClickCall() = profileCache?.teacher?.phone?.let { phoneNumber ->
        _message.update {
            TeacherProfileMessageState.CallConfirmation(phoneNumber) {
                // call here
            }
        }
    }

    private fun onClickEmail() = profileCache?.teacher?.email?.let { email ->
        if (email.isEmpty()) return@let
        _message.update {
            TeacherProfileMessageState.EmailConfirmation(email) {
                // send email here
            }
        }
    }

    private fun onClickSignOut() {
        if (profileCache?.isSignedIn != true) return
        _message.update { TeacherProfileMessageState.ConfirmSignOut(::signOut) }
    }

    private fun onClickTab(index: Int) {
        uiStateMachine.updateSelectedTab(index)
    }

    private fun onClickRefresh() = loadProfile()

    private fun onClickChangeImage() {
        if (profileCache?.isSignedIn != true) return

        profileCache?.teacher?.let { teacher ->
            _navigation.update {
                TeacherProfileNavigationState.ImageUploadScreen(
                    userId = teacher.userId,
                    userType = UserType.TEACHER,
                )
            }
        }
    }

    private fun onClickEditBio() {
        if (profileCache?.isSignedIn != true) return

        profileCache?.teacher?.let { teacher ->
            _message.update {
                TeacherProfileMessageState.InputBio(teacher.bio.orEmpty(), ::changeBio)
            }
        }
    }

    private fun onClickEdit(selectedTabIndex: Int) {
        if (profileCache?.isSignedIn != true) return

        profileCache?.teacher?.let { teacher ->
            when (selectedTabIndex) {
                0 -> ProfileEditMode.ACADEMIC
                1 -> ProfileEditMode.CONNECT
                else -> null
            }?.let { action ->
                _navigation.update {
                    TeacherProfileNavigationState.TeacherProfileEditScreen(
                        userId = teacher.userId,
                        action = action,
                    )
                }
            }
        }
    }

    private fun onClickChangePassword() {
        if (profileCache?.isSignedIn != true) return

        _navigation.update { TeacherProfileNavigationState.ChangePasswordScreen }
    }

    private fun onClickMyDeviceList() {
        if (profileCache?.isSignedIn != true) return

        profileCache?.teacher?.let { teacher ->
            _navigation.update {
                TeacherProfileNavigationState.MyDeviceListScreen(
                    userId = teacher.userId,
                    userType = UserType.TEACHER,
                )
            }
        }
    }

    private fun onClickDeleteAccount() {
        if (profileCache?.isSignedIn == true) {
            _navigation.update { TeacherProfileNavigationState.DeleteAccountScreen }
        }
    }

    private var isChangingPhoto = false
    fun changeProfileImage(imageUrl: String) {
        profileCache?.let { cache ->
            if (isChangingPhoto || !cache.isSignedIn) return

            isChangingPhoto = true
            _message.update { TeacherProfileMessageState.Loading(cancelable = false) }
            viewModelScope.launch {
                runCatching {
                    teacherRepo.changeProfileImage(cache.teacher, imageUrl)
                }.onSuccess {
                    isChangingPhoto = false
                    _message.update {
                        TeacherProfileMessageState.Success("Profile photo changed successfully!")
                    }
                    loadProfile()
                }.onFailure {
                    isChangingPhoto = false
                    val message = it.message ?: "Failed to change photo. Please try again."
                    _message.update { TeacherProfileMessageState.Error(message) }
                }
            }
        }
    }

    fun changeBio(newBio: String) {
        val teacher = profileCache?.teacher ?: return
        _message.update { TeacherProfileMessageState.Loading(cancelable = false) }
        viewModelScope.launch {
            runCatching {
                teacherRepo.changeBio(teacher, newBio)
            }.onSuccess {
                val message = "Bio updated successfully"
                _message.update { TeacherProfileMessageState.Success(message) }
                loadProfile()
            }.onFailure {
                val message = it.message ?: "Failed to update bio. Please try again."
                _message.update { TeacherProfileMessageState.Error(message) }
            }
        }
    }

    fun signOut() {
        val userType = settingsRepository.getUserType() ?: return
        _message.update { TeacherProfileMessageState.Loading(cancelable = false) }
        viewModelScope.launch {
            runCatching {
                authRepo.signOut(userType, fromAllDevice = false)
                messageHandled()
                loadProfile()
            }.onFailure {
                val message = it.message ?: "Signing out failed. Please try again."
                _message.update { TeacherProfileMessageState.Error(message) }
            }
        }
    }
}
