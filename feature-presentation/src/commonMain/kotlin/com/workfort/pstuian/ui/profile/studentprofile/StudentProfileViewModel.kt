package com.workfort.pstuian.ui.profile.studentprofile

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.data.infrastructure.repository.StudentRepositoryImpl
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.UserPresenceRepository
import com.workfort.pstuian.featuredomain.usecase.GetStudentProfileUserUseCase
import com.workfort.pstuian.ui.common.uistate.InitializationMode
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.profile.common.UserPresenceDisplayDataMapper
import com.workfort.pstuian.ui.profile.common.displaydata.UserPresenceDisplayData
import com.workfort.pstuian.ui.profile.common.state.ProfileScreenUiStateMachine
import com.workfort.pstuian.ui.profile.common.state.ProfileUiEvent
import com.workfort.pstuian.ui.profile.common.state.ProfileUiState
import com.workfort.pstuian.ui.profile.studentprofile.state.StudentProfileMessageState
import com.workfort.pstuian.ui.profile.studentprofile.state.StudentProfileNavigationState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StudentProfileViewModel(
    private val userId: Int,
    private val studentRepo: StudentRepositoryImpl,
    private val authRepo: AuthRepository,
    private val userPresenceRepository: UserPresenceRepository,
    private val getStudentProfileUserUseCase: GetStudentProfileUserUseCase,
    private val studentProfileDisplayDataMapper: StudentProfileDisplayDataMapper,
    private val userPresenceDisplayDataMapper: UserPresenceDisplayDataMapper,
    private val uiStateMachine: ProfileScreenUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<ProfileUiState>(
    uiStateMachine = uiStateMachine,
    initializationMode = InitializationMode.Manual,
) {

    private val _message = MutableStateFlow<StudentProfileMessageState?>(null)
    val message: StateFlow<StudentProfileMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<StudentProfileNavigationState?>(null)
    val navigation: StateFlow<StudentProfileNavigationState?> = _navigation.asStateFlow()

    private var profileCache: UserProfile.StudentProfile? = null

    private var userPresenceCollectionJob: Job? = null

    override fun onUiReady() {
        loadProfile()
    }

    fun onUiEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.BackClicked -> _navigation.update { StudentProfileNavigationState.GoBack }
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
            is ProfileUiEvent.MyBloodDonationListClicked -> onClickMyBloodDonationList()
            is ProfileUiEvent.ChangePasswordClicked -> onClickChangePassword()
            is ProfileUiEvent.DownloadCvClicked -> onClickDownloadCv(event.url)
            is ProfileUiEvent.UploadCvClicked -> onClickUploadCv()
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
            getStudentProfileUserUseCase(studentId = userId)
                .onSuccess { profile ->
                    profileCache = profile
                    uiStateMachine.showProfile(
                        headerDisplayData = studentProfileDisplayDataMapper.mapHeaderData(profile),
                        academicContents = studentProfileDisplayDataMapper.mapAcademicContents(profile),
                        connectContents = studentProfileDisplayDataMapper.mapConnectContents(profile),
                        isSignedIn = profile.isSignedIn,
                    )
                    observeUserPresence(profile.student.authUserId, profile.isSignedIn)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load student profile"
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
        _message.update { StudentProfileMessageState.Success("Follow feature will be available soon!") }
    }

    private fun onClickImage(url: String) {
        _navigation.update { StudentProfileNavigationState.ImagePreviewScreen(url) }
    }

    private fun onClickCall() = profileCache?.student?.phone?.let { phoneNumber ->
        _message.update {
            StudentProfileMessageState.CallConfirmation(phoneNumber) {
                // call here
            }
        }
    }

    private fun onClickEmail() = profileCache?.student?.email?.let { email ->
        _message.update {
            StudentProfileMessageState.EmailConfirmation(email) {
                // send email here
            }
        }
    }

    private fun onClickSignOut() {
        if (profileCache?.isSignedIn != true) return
        _message.update {
            StudentProfileMessageState.ConfirmSignOut {
                messageHandled()
                signOut()
            }
        }
    }

    private fun onClickTab(index: Int) {
        uiStateMachine.updateSelectedTab(index)
    }

    private fun onClickRefresh() = loadProfile()

    private fun onClickChangeImage() {
        if (profileCache?.isSignedIn != true) return

        profileCache?.student?.let { student ->
            _navigation.update {
                StudentProfileNavigationState.ImageUploadScreen(
                    userId = student.authUserId,
                    userType = UserType.STUDENT,
                )
            }
        }
    }

    private fun onClickEditBio() {
        if (profileCache?.isSignedIn != true) return

        profileCache?.student?.let { student ->
            val currentBio = student.bio.orEmpty()
            _message.update {
                StudentProfileMessageState.InputBio(currentBio, ::changeBio)
            }
        }
    }

    private fun onClickEdit() {
        if (profileCache?.isSignedIn != true) return

        profileCache?.student?.let { student ->
            _navigation.update { StudentProfileNavigationState.StudentProfileEditScreen(student.userId) }
        }
    }

    private fun onClickMyBloodDonationList() {
        if (profileCache?.isSignedIn != true) return

        profileCache?.student?.let { student ->
            _navigation.update {
                StudentProfileNavigationState.MyBloodDonationListScreen(
                    userId = student.authUserId,
                    userType = UserType.STUDENT,
                )
            }
        }
    }

    private fun onClickChangePassword() {
        if (profileCache?.isSignedIn != true) return

        _navigation.update { StudentProfileNavigationState.ChangePasswordScreen }
    }

    private fun onClickDownloadCv(url: String) {
        profileCache?.student?.let { student ->
            _navigation.update {
                StudentProfileNavigationState.DownloadCvScreen(
                    userId = student.authUserId,
                    userType = UserType.STUDENT,
                    url = url,
                )
            }
        }
    }

    private fun onClickUploadCv() {
        if (profileCache?.isSignedIn != true) return

        profileCache?.student?.let { student ->
            _navigation.update {
                StudentProfileNavigationState.UploadCvScreen(
                    userId = student.authUserId,
                    userType = UserType.STUDENT,
                )
            }
        }
    }

    private fun onClickMyCheckInList() {
        if (profileCache?.isSignedIn != true) return

        profileCache?.student?.let { student ->
            _navigation.update {
                StudentProfileNavigationState.MyCheckInListScreen(
                    userId = student.authUserId,
                    userType = UserType.STUDENT,
                )
            }
        }
    }

    private fun onClickMyDeviceList() {
        if (profileCache?.isSignedIn != true) return

        profileCache?.student?.let { student ->
            _navigation.update {
                StudentProfileNavigationState.MyDeviceListScreen(
                    userId = student.authUserId,
                    userType = UserType.STUDENT,
                )
            }
        }
    }

    private fun onClickDeleteAccount() {
        if (profileCache?.isSignedIn == true) {
            _navigation.update { StudentProfileNavigationState.DeleteAccountScreen }
        }
    }

    private var isChangingPhoto = false
    fun changeProfileImage(imageUrl: String) {
        profileCache?.let { cache ->
            if (isChangingPhoto || !cache.isSignedIn) return

            isChangingPhoto = true
            _message.update { StudentProfileMessageState.Loading(cancelable = false) }
            viewModelScope.launch {
                runCatching {
                    studentRepo.changeProfileImage(cache.student.authUserId, imageUrl)
                }.onSuccess {
                    isChangingPhoto = false
                    _message.update {
                        StudentProfileMessageState.Success("Profile photo changed successfully!")
                    }
                    loadProfile()
                }.onFailure {
                    isChangingPhoto = false
                    val message = it.message ?: "Failed to change photo. Please try again."
                    _message.update { StudentProfileMessageState.Error(message) }
                }
            }
        }
    }

    fun changeBio(newBio: String) {
        val student = profileCache?.student ?: return
        _message.update { StudentProfileMessageState.Loading(cancelable = false) }
        viewModelScope.launch {
            runCatching {
                studentRepo.changeBio(student.authUserId, newBio)
            }.onSuccess {
                val message = "Bio updated successfully"
                _message.update { StudentProfileMessageState.Success(message) }
                loadProfile()
            }.onFailure {
                val message = it.message ?: "Failed to update bio. Please try again."
                _message.update { StudentProfileMessageState.Error(message) }
            }
        }
    }

    fun signOut() {
        cancelUserPresenceObservation()
        _message.update { StudentProfileMessageState.Loading(cancelable = false) }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            authRepo.signOut(UserType.STUDENT)
                .onSuccess {
                    messageHandled()
                    loadProfile()
                }
                .onFailure {
                    messageHandled()
                    val message = it.message ?: "Signing out failed. Please try again."
                    _message.update { StudentProfileMessageState.Error(message) }
                }
        }
    }
}
