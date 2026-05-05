package com.workfort.pstuian.ui.profile.employeeprofile

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.UserPresenceRepository
import com.workfort.pstuian.featuredomain.usecase.GetEmployeeProfileUserUseCase
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.profile.common.UserPresenceDisplayDataMapper
import com.workfort.pstuian.ui.profile.common.displaydata.UserPresenceDisplayData
import com.workfort.pstuian.ui.profile.common.state.ProfileScreenUiStateMachine
import com.workfort.pstuian.ui.profile.common.state.ProfileUiEvent
import com.workfort.pstuian.ui.profile.common.state.ProfileUiState
import com.workfort.pstuian.ui.profile.employeeprofile.state.EmployeeProfileMessageState
import com.workfort.pstuian.ui.profile.employeeprofile.state.EmployeeProfileNavigationState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmployeeProfileViewModel(
    private val userId: Int,
    private val authRepo: AuthRepository,
    private val userPresenceRepository: UserPresenceRepository,
    private val getEmployeeProfileUserUseCase: GetEmployeeProfileUserUseCase,
    private val employeeProfileDisplayDataMapper: EmployeeProfileDisplayDataMapper,
    private val userPresenceDisplayDataMapper: UserPresenceDisplayDataMapper,
    private val uiStateMachine: ProfileScreenUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<ProfileUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<EmployeeProfileMessageState?>(null)
    val message: StateFlow<EmployeeProfileMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<EmployeeProfileNavigationState?>(null)
    val navigation: StateFlow<EmployeeProfileNavigationState?> = _navigation.asStateFlow()

    private var profileCache: UserProfile.EmployeeProfile? = null

    private var userPresenceCollectionJob: Job? = null

    override fun onUiReady() {
        loadProfile()
    }

    fun onUiEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.BackClicked -> _navigation.update { EmployeeProfileNavigationState.GoBack }
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
            is ProfileUiEvent.BloodDonationHistoryClicked -> Unit
            is ProfileUiEvent.ChangePasswordClicked -> onClickChangePassword()
            is ProfileUiEvent.DownloadCvClicked -> Unit
            is ProfileUiEvent.UploadCvClicked -> Unit
            is ProfileUiEvent.CheckInHistoryClicked -> Unit
            is ProfileUiEvent.DeleteAccountClicked -> onClickDeleteAccount()
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
            getEmployeeProfileUserUseCase(userId)
                .onSuccess { profile ->
                    profileCache = profile
                    uiStateMachine.showProfile(
                        headerDisplayData = employeeProfileDisplayDataMapper.mapHeaderData(profile),
                        academicContents = employeeProfileDisplayDataMapper.mapAcademicContents(profile),
                        connectContents = employeeProfileDisplayDataMapper.mapConnectContents(profile),
                        isSignedIn = profile.isSignedIn,
                    )
                    observeUserPresence(profile.employee.authUserId, profile.isSignedIn)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load employee profile"
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
        _message.update { EmployeeProfileMessageState.Success("Follow feature will be available soon!") }
    }

    private fun onClickImage(url: String) {
        _navigation.update { EmployeeProfileNavigationState.ImagePreviewScreen(url) }
    }

    private fun onClickCall() = profileCache?.employee?.phone?.let { phoneNumber ->
        if (phoneNumber.isNotEmpty()) {
            _message.update {
                EmployeeProfileMessageState.CallConfirmation(phoneNumber) {
                    // call here
                }
            }
        }
    }

    private fun onClickEmail() = profileCache?.employee?.email?.let { email ->
        if (email.isEmpty()) return@let
        _message.update {
            EmployeeProfileMessageState.EmailConfirmation(email) {
                // send email here
            }
        }
    }

    private fun onClickSignOut() {
        if (profileCache?.isSignedIn != true) return
        _message.update { EmployeeProfileMessageState.ConfirmSignOut(::signOut) }
    }

    private fun onClickTab(index: Int) {
        uiStateMachine.updateSelectedTab(index)
    }

    private fun onClickRefresh() = loadProfile()

    private fun onClickChangeImage() {
        if (profileCache?.isSignedIn != true) return

        profileCache?.employee?.let { employee ->
            _navigation.update {
                EmployeeProfileNavigationState.ImageUploadScreen(userId = employee.userId)
            }
        }
    }

    private fun onClickEditBio() {
        if (profileCache?.isSignedIn != true) return

        profileCache?.employee?.let { employee ->
            _message.update {
                EmployeeProfileMessageState.InputBio(employee.bio.orEmpty(), ::changeBio)
            }
        }
    }

    private fun onClickEdit() {
        if (profileCache?.isSignedIn != true) return

        profileCache?.employee?.let { employee ->
            _navigation.update {
                EmployeeProfileNavigationState.EmployeeProfileEditScreen(userId = employee.userId)
            }
        }
    }

    private fun onClickChangePassword() {
        if (profileCache?.isSignedIn != true) return

        _navigation.update { EmployeeProfileNavigationState.ChangePasswordScreen }
    }

    private fun onClickDeleteAccount() {
        if (profileCache?.isSignedIn == true) {
            _navigation.update { EmployeeProfileNavigationState.DeleteAccountScreen }
        }
    }

    fun changeBio(newBio: String) {
        _message.update {
            EmployeeProfileMessageState.Error("Bio update is not supported for this profile yet.")
        }
    }

    fun signOut(fromAllDevice: Boolean) {
        cancelUserPresenceObservation()
        _message.update { EmployeeProfileMessageState.Loading(cancelable = false) }
        viewModelScope.launch {
            runCatching {
                messageHandled()
                authRepo.signOut(fromAllDevice = fromAllDevice)
                _navigation.update { EmployeeProfileNavigationState.ResetToHome }
            }.onFailure {
                val message = it.message ?: "Signing out failed. Please try again."
                _message.update { EmployeeProfileMessageState.Error(message) }
            }
        }
    }
}
