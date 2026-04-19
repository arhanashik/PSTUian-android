package com.workfort.pstuian.ui.employeeprofile

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.data.infrastructure.repository.FacultyRepositoryImpl
import com.workfort.pstuian.featuredomain.model.EmployeeProfile
import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.ui.common.uistate.InitializationMode
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.employeeprofile.state.EmployeeProfileMessageState
import com.workfort.pstuian.ui.employeeprofile.state.EmployeeProfileNavigationState
import com.workfort.pstuian.ui.employeeprofile.state.EmployeeProfileUiEvent
import com.workfort.pstuian.ui.employeeprofile.state.EmployeeProfileUiState
import com.workfort.pstuian.ui.employeeprofile.state.ProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmployeeProfileViewModel(
    private val userId: Int,
    private val facultyRepo: FacultyRepositoryImpl,
    private val authRepo: AuthRepository,
    private val uiStateMachine: EmployeeProfileUiStateMachine,
) : UiStateMachineViewModel<EmployeeProfileUiState>(
    uiStateMachine,
    initializationMode = InitializationMode.JustOnce,
) {

    private val _message = MutableStateFlow<EmployeeProfileMessageState?>(null)
    val message: StateFlow<EmployeeProfileMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<EmployeeProfileNavigationState?>(null)
    val navigation: StateFlow<EmployeeProfileNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {
        loadProfile()
    }

    fun onUiEvent(event: EmployeeProfileUiEvent) {
        when (event) {
            is EmployeeProfileUiEvent.LoadProfile -> loadProfile()
            is EmployeeProfileUiEvent.BackClicked -> onClickBack()
            is EmployeeProfileUiEvent.ImageClicked -> onClickImage(event.url)
            is EmployeeProfileUiEvent.CallClicked -> onClickCall()
            is EmployeeProfileUiEvent.EmailClicked -> onClickEmail()
            is EmployeeProfileUiEvent.SignOutClicked -> onClickSignOut()
            is EmployeeProfileUiEvent.TabClicked -> onClickTab(event.index)
            is EmployeeProfileUiEvent.RefreshClicked -> onClickRefresh()
            is EmployeeProfileUiEvent.ChangeImageClicked -> onClickChangeImage()
            is EmployeeProfileUiEvent.EditBioClicked -> onClickEditBio()
            is EmployeeProfileUiEvent.EditClicked -> onClickEdit(event.selectedTabIndex)
            is EmployeeProfileUiEvent.ChangePasswordClicked -> onClickChangePassword()
            is EmployeeProfileUiEvent.MyDeviceListClicked -> onClickMyDeviceList()
            is EmployeeProfileUiEvent.DeleteAccountClicked -> onClickDeleteAccount()
            is EmployeeProfileUiEvent.ChangeProfileImage -> changeProfileImage(event.imageUrl)
            is EmployeeProfileUiEvent.ChangeBio -> changeBio(event.newBio)
            is EmployeeProfileUiEvent.SignOut -> signOut()
        }
    }

    fun messageHandled() = _message.update { null }

    fun navigationHandled() = _navigation.update { null }

    private fun onClickBack() = _navigation.update { EmployeeProfileNavigationState.GoBack }

    private fun onClickImage(url: String) {
        _navigation.update { EmployeeProfileNavigationState.ImagePreviewScreen(url) }
    }

    private fun onClickCall() = profileCache()?.employee?.phone?.let { phoneNumber ->
        if (phoneNumber.isNotEmpty()) {
            _message.update {
                EmployeeProfileMessageState.CallConfirmation(phoneNumber) {
                    // Handle call in screen or via navigation
                }
            }
        }
    }

    private fun onClickEmail() {
        // EmployeeEntity does not have an email field yet
    }

    private fun onClickSignOut() {
        if (profileCache()?.isSignedIn == true) {
            _message.update {
                EmployeeProfileMessageState.ConfirmSignOut {
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
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.employee?.let { employee ->
            _navigation.update {
                EmployeeProfileNavigationState.ImageUploadScreen(
                    userId = employee.id.toString(),
                    userType = UserType.EMPLOYEE,
                )
            }
        }
    }

    private fun onClickEditBio() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.employee?.let { employee ->
            _message.update {
                EmployeeProfileMessageState.InputBio(employee.bio.orEmpty(), ::changeBio)
            }
        }
    }

    private fun onClickEdit(selectedTabIndex: Int) {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.employee?.let { employee ->
            when (selectedTabIndex) {
                0 -> ProfileEditMode.ACADEMIC
                1 -> ProfileEditMode.CONNECT
                else -> null
            }?.let { mode ->
                _navigation.update {
                    EmployeeProfileNavigationState.EmployeeProfileEditScreen(
                        userId = employee.id.toString(),
                        action = mode,
                    )
                }
            }
        }
    }

    private fun onClickChangePassword() {
        if (profileCache()?.isSignedIn != true) return
        _navigation.update { EmployeeProfileNavigationState.ChangePasswordScreen }
    }

    private fun onClickMyDeviceList() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.employee?.let { employee ->
            _navigation.update {
                EmployeeProfileNavigationState.MyDeviceListScreen(
                    userId = employee.id.toString(),
                    userType = UserType.EMPLOYEE,
                )
            }
        }
    }

    private fun onClickDeleteAccount() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.employee?.let { employee ->
            _navigation.update {
                EmployeeProfileNavigationState.DeleteAccountScreen(
                    userId = employee.id.toString(),
                    userType = UserType.EMPLOYEE,
                )
            }
        }
    }

    private fun changeProfileImage(imageUrl: String) {
        // Implementation for changing profile image
    }

    fun changeBio(newBio: String) {
        // Implementation for changing bio
    }

    fun signOut() {
        _message.update { EmployeeProfileMessageState.Loading(cancelable = false) }
        viewModelScope.launch {
            runCatching {
                authRepo.signOut(fromAllDevice = false)
                messageHandled()
                loadProfile()
            }.onFailure {
                val message = it.message ?: "Signing out failed. Please try again."
                _message.update { EmployeeProfileMessageState.Error(message) }
            }
        }
    }

    private fun profileCache(): EmployeeProfile? {
        return when (val state = uiState.value.profileState) {
            is ProfileState.Available -> state.profile
            else -> null
        }
    }

    fun loadProfile() {
        getProfile(userId)
    }

    private fun getProfile(employeeId: Int) {
        uiStateMachine.showProfileLoading()
        viewModelScope.launch {
            runCatching {
                facultyRepo.getEmployeeProfile(employeeId)
            }.onSuccess {
                uiStateMachine.showProfile(it)
            }.onFailure {
                val message = it.message ?: "Failed to load employee profile"
                uiStateMachine.showProfileError(message)
            }
        }
    }
}
