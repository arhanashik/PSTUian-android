package com.workfort.pstuian.ui.studentprofile

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.data.infrastructure.repository.StudentRepositoryImpl
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.StudentProfile
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.featuredomain.usecase.GetStudentProfileUserUseCase
import com.workfort.pstuian.ui.common.uistate.InitializationMode
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.studentprofile.state.ProfileState
import com.workfort.pstuian.ui.studentprofile.state.StudentProfileMessageState
import com.workfort.pstuian.ui.studentprofile.state.StudentProfileNavigationState
import com.workfort.pstuian.ui.studentprofile.state.StudentProfileUiEvent
import com.workfort.pstuian.ui.studentprofile.state.StudentProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StudentProfileViewModel(
    private val userId: Int,
    private val studentRepo: StudentRepositoryImpl,
    private val authRepo: AuthRepository,
    private val settingsRepository: SettingsRepository,
    private val getStudentProfileUserUseCase: GetStudentProfileUserUseCase,
    private val uiStateMachine: StudentProfileUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<StudentProfileUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<StudentProfileMessageState?>(null)
    val message: StateFlow<StudentProfileMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<StudentProfileNavigationState?>(null)
    val navigation: StateFlow<StudentProfileNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {
        loadProfile()
    }

    fun onUiEvent(event: StudentProfileUiEvent) {
        when (event) {
            is StudentProfileUiEvent.BackClicked -> _navigation.update { StudentProfileNavigationState.GoBack }
            is StudentProfileUiEvent.FollowClicked -> onClickFollow()
            is StudentProfileUiEvent.ImageClicked -> onClickImage(event.url)
            is StudentProfileUiEvent.CallClicked -> onClickCall()
            is StudentProfileUiEvent.EmailClicked -> onClickEmail()
            is StudentProfileUiEvent.SignOutClicked -> onClickSignOut()
            is StudentProfileUiEvent.TabClicked -> onClickTab(event.index)
            is StudentProfileUiEvent.RefreshClicked -> onClickRefresh()
            is StudentProfileUiEvent.ChangeImageClicked -> onClickChangeImage()
            is StudentProfileUiEvent.EditBioClicked -> onClickEditBio()
            is StudentProfileUiEvent.EditClicked -> onClickEdit(event.selectedTabIndex)
            is StudentProfileUiEvent.MyBloodDonationListClicked -> onClickMyBloodDonationList()
            is StudentProfileUiEvent.ChangePasswordClicked -> onClickChangePassword()
            is StudentProfileUiEvent.DownloadCvClicked -> onClickDownloadCv(event.url)
            is StudentProfileUiEvent.UploadCvClicked -> onClickUploadCv()
            is StudentProfileUiEvent.MyCheckInListClicked -> onClickMyCheckInList()
            is StudentProfileUiEvent.MyDeviceListClicked -> onClickMyDeviceList()
            is StudentProfileUiEvent.DeleteAccountClicked -> onClickDeleteAccount()
            is StudentProfileUiEvent.ChangeProfileImage -> changeProfileImage(event.imageUrl)
        }
    }

    fun messageHandled() = _message.update { null }

    fun navigationHandled() = _navigation.update { null }

    private fun loadProfile() {
        uiStateMachine.showProfileLoading()
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            getStudentProfileUserUseCase(userId)
                .onSuccess { profile ->
                    val isSignedIn = authRepo.getAuthUser()?.email == profile.student.email
                    uiStateMachine.showProfile(profile, isSignedIn)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load student profile"
                    uiStateMachine.showProfileError(message)
                }
        }
    }

    private fun onClickFollow() {
        _message.update { StudentProfileMessageState.Success("Follow feature will be available soon!") }
    }

    private fun onClickImage(url: String) {
        _navigation.update { StudentProfileNavigationState.ImagePreviewScreen(url) }
    }

    private fun onClickCall() = profileCache()?.student?.phone?.let { phoneNumber ->
        _message.update {
            StudentProfileMessageState.CallConfirmation(phoneNumber) {
                // call here
            }
        }
    }

    private fun onClickEmail() = profileCache()?.student?.email?.let { email ->
        _message.update {
            StudentProfileMessageState.EmailConfirmation(email) {
                // send email here
            }
        }
    }

    private fun onClickSignOut() {
        if (isSignedIn()) {
            _message.update {
                StudentProfileMessageState.ConfirmSignOut {
                    signOut()
                }
            }
        }
    }

    private fun onClickTab(index: Int) {
        uiStateMachine.updateSelectedTab(index)
    }

    private fun onClickRefresh() = loadProfile()

    private fun onClickChangeImage() {
        if (!isSignedIn()) return

        profileCache()?.student?.let { student ->
            _navigation.update {
                StudentProfileNavigationState.ImageUploadScreen(
                    userId = student.userId,
                    userType = UserType.STUDENT,
                )
            }
        }
    }

    private fun onClickEditBio() {
        if (!isSignedIn()) return

        profileCache()?.student?.let { student ->
            val currentBio = student.bio.orEmpty()
            _message.update {
                StudentProfileMessageState.InputBio(currentBio, ::changeBio)
            }
        }
    }

    private fun onClickEdit(selectedTabIndex: Int) {
        if (!isSignedIn()) return

        profileCache()?.student?.let { student ->
            when (selectedTabIndex) {
                0 -> ProfileEditMode.ACADEMIC
                1 -> ProfileEditMode.CONNECT
                else -> null
            }?.let { action ->
                _navigation.update {
                    StudentProfileNavigationState.StudentProfileEditScreen(
                        userId = student.studentId,
                        action = action,
                    )
                }
            }
        }
    }

    private fun onClickMyBloodDonationList() {
        if (!isSignedIn()) return

        profileCache()?.student?.let { student ->
            _navigation.update {
                StudentProfileNavigationState.MyBloodDonationListScreen(
                    userId = student.userId,
                    userType = UserType.STUDENT,
                )
            }
        }
    }

    private fun onClickChangePassword() {
        if (!isSignedIn()) return

        _navigation.update { StudentProfileNavigationState.ChangePasswordScreen }
    }

    private fun onClickDownloadCv(url: String) {
        profileCache()?.student?.let { student ->
            _navigation.update {
                StudentProfileNavigationState.DownloadCvScreen(
                    userId = student.userId,
                    userType = UserType.STUDENT,
                    url = url,
                )
            }
        }
    }

    private fun onClickUploadCv() {
        if (!isSignedIn()) return

        profileCache()?.student?.let { student ->
            _navigation.update {
                StudentProfileNavigationState.UploadCvScreen(
                    userId = student.userId,
                    userType = UserType.STUDENT,
                )
            }
        }
    }

    private fun onClickMyCheckInList() {
        if (!isSignedIn()) return

        profileCache()?.student?.let { student ->
            _navigation.update {
                StudentProfileNavigationState.MyCheckInListScreen(
                    userId = student.userId,
                    userType = UserType.STUDENT,
                )
            }
        }
    }

    private fun onClickMyDeviceList() {
        if (!isSignedIn()) return

        profileCache()?.student?.let { student ->
            _navigation.update {
                StudentProfileNavigationState.MyDeviceListScreen(
                    userId = student.userId,
                    userType = UserType.STUDENT,
                )
            }
        }
    }

    private fun onClickDeleteAccount() {
        if (!isSignedIn()) return

        _navigation.update { StudentProfileNavigationState.DeleteAccountScreen }
    }

    private fun profileCache(): StudentProfile? {
        return when (val state = uiState.value.profileState) {
            is ProfileState.Available -> state.profile
            else -> null
        }
    }

    private fun isSignedIn(): Boolean {
        return authRepo.getAuthUser()?.email == profileCache()?.student?.email
    }

    private var isChangingPhoto = false
    fun changeProfileImage(imageUrl: String) {
        profileCache()?.let { cache ->
            if (isChangingPhoto || isSignedIn().not()) {
                return
            }
            isChangingPhoto = true
            _message.update { StudentProfileMessageState.Loading(cancelable = false) }
            viewModelScope.launch {
                runCatching {
                    studentRepo.changeProfileImage(cache.student.userId, imageUrl)
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
        val student = profileCache()?.student ?: return
        _message.update { StudentProfileMessageState.Loading(cancelable = false) }
        viewModelScope.launch {
            runCatching {
                studentRepo.changeBio(student.userId, newBio)
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
        val userType = settingsRepository.getUserType() ?: return
        _message.update { StudentProfileMessageState.Loading(cancelable = false) }
        viewModelScope.launch {
            runCatching {
                authRepo.signOut(userType, fromAllDevice = false)
                messageHandled()
                loadProfile()
            }.onFailure {
                val message = it.message ?: "Signing out failed. Please try again."
                _message.update { StudentProfileMessageState.Error(message) }
            }
        }
    }
}
