package com.workfort.pstuian.ui.profile.teacherprofile

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.data.infrastructure.repository.TeacherRepositoryImpl
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.UserPresenceRepository
import com.workfort.pstuian.featuredomain.usecase.GetTeacherProfileUserUseCase
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.profile.common.UserPresenceDisplayDataMapper
import com.workfort.pstuian.ui.profile.common.displaydata.UserPresenceDisplayData
import com.workfort.pstuian.ui.profile.common.state.ProfileScreenUiStateMachine
import com.workfort.pstuian.ui.profile.common.state.ProfileUiEvent
import com.workfort.pstuian.ui.profile.common.state.ProfileUiState
import com.workfort.pstuian.ui.profile.teacherprofile.state.TeacherProfileMessageState
import com.workfort.pstuian.ui.profile.teacherprofile.state.TeacherProfileNavigationState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TeacherProfileViewModel(
    private val userId: Int,
    private val teacherRepo: TeacherRepositoryImpl,
    private val authRepo: AuthRepository,
    private val userPresenceRepository: UserPresenceRepository,
    private val getTeacherProfileUserUseCase: GetTeacherProfileUserUseCase,
    private val teacherProfileDisplayDataMapper: TeacherProfileDisplayDataMapper,
    private val userPresenceDisplayDataMapper: UserPresenceDisplayDataMapper,
    private val uiStateMachine: ProfileScreenUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<ProfileUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<TeacherProfileMessageState?>(null)
    val message: StateFlow<TeacherProfileMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<TeacherProfileNavigationState?>(null)
    val navigation: StateFlow<TeacherProfileNavigationState?> = _navigation.asStateFlow()

    private var profileCache: UserProfile.TeacherProfile? = null

    private var userPresenceCollectionJob: Job? = null

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
                is ProfileUiEvent.EditClicked -> onClickEdit()
            is ProfileUiEvent.MyBloodDonationListClicked -> Unit
            is ProfileUiEvent.ChangePasswordClicked -> onClickChangePassword()
            is ProfileUiEvent.DownloadCvClicked -> Unit
            is ProfileUiEvent.UploadCvClicked -> Unit
            is ProfileUiEvent.MyCheckInListClicked -> onClickMyCheckInList()
            is ProfileUiEvent.MyDeviceListClicked -> onClickMyDeviceList()
            is ProfileUiEvent.DeleteAccountClicked -> onClickDeleteAccount()
            is ProfileUiEvent.ChangeProfileImage -> changeProfileImage(event.imageUrl)
        }
    }

    fun messageHandled() = _message.update { null }

    fun navigationHandled() = _navigation.update { null }

    private fun cancelUserPresenceObservation() {
        userPresenceCollectionJob?.cancel()
        userPresenceCollectionJob = null
    }

    override fun onCleared() {
        cancelUserPresenceObservation()
        super.onCleared()
    }

    private fun loadProfile() {
        uiStateMachine.showProfileLoading()
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            getTeacherProfileUserUseCase(userId)
                .onSuccess { profile ->
                    profileCache = profile
                    uiStateMachine.showProfile(
                        headerDisplayData = teacherProfileDisplayDataMapper.mapHeaderData(profile),
                        academicContents = teacherProfileDisplayDataMapper.mapAcademicContents(profile),
                        connectContents = teacherProfileDisplayDataMapper.mapConnectContents(profile),
                        isSignedIn = profile.isSignedIn,
                    )
                    observeUserPresence(profile.teacher.authUserId, profile.isSignedIn)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load teacher profile"
                    uiStateMachine.showProfileError(message)
                }
        }
    }

    private fun observeUserPresence(userId: String, isSignedIn: Boolean) {
        cancelUserPresenceObservation()
        if (isSignedIn) {
            uiStateMachine.updateUserPresenceData(UserPresenceDisplayData(isOnline = true))
            return
        }
        if (!authRepo.isUserSignedIn()) {
            uiStateMachine.updateUserPresenceData(userPresenceDisplayDataMapper.map(null))
            return
        }
        userPresenceCollectionJob = viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            userPresenceRepository.observeUserPresence(userId).collectLatest {
                val userPresenceDisplayData = userPresenceDisplayDataMapper.map(it)
                uiStateMachine.updateUserPresenceData(userPresenceDisplayData)
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
                TeacherProfileNavigationState.ImageUploadScreen(userId = teacher.userId)
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

    private fun onClickEdit() {
        if (profileCache?.isSignedIn != true) return

        profileCache?.teacher?.let { teacher ->
            _navigation.update {
                TeacherProfileNavigationState.TeacherProfileEditScreen(userId = teacher.userId)
            }
        }
    }

    private fun onClickChangePassword() {
        if (profileCache?.isSignedIn != true) return

        _navigation.update { TeacherProfileNavigationState.ChangePasswordScreen }
    }

    private fun onClickMyCheckInList() {
        if (profileCache?.isSignedIn != true) return

        profileCache?.teacher?.let { student ->
            _navigation.update {
                TeacherProfileNavigationState.MyCheckInListScreen(userId = student.userId)
            }
        }
    }

    private fun onClickMyDeviceList() {
        if (profileCache?.isSignedIn != true) return

        profileCache?.teacher?.let { teacher ->
            _navigation.update {
                TeacherProfileNavigationState.MyDeviceListScreen(userId = teacher.userId)
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
                    teacherRepo.changeProfileImage(imageUrl)
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
        cancelUserPresenceObservation()
        _message.update { TeacherProfileMessageState.Loading(cancelable = false) }
        viewModelScope.launch {
            runCatching {
                messageHandled()
                authRepo.signOut()
                _navigation.update { TeacherProfileNavigationState.ResetToHome }
            }.onFailure {
                val message = it.message ?: "Signing out failed. Please try again."
                _message.update { TeacherProfileMessageState.Error(message) }
            }
        }
    }
}
