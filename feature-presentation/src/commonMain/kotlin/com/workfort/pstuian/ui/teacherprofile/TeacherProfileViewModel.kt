package com.workfort.pstuian.ui.teacherprofile

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.InitializationMode
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.data.infrastructure.repository.TeacherRepositoryImpl
import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.TeacherProfile
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.ui.teacherprofile.state.ProfileState
import com.workfort.pstuian.ui.teacherprofile.state.TeacherProfileMessageState
import com.workfort.pstuian.ui.teacherprofile.state.TeacherProfileNavigationState
import com.workfort.pstuian.ui.teacherprofile.state.TeacherProfileUiEvent
import com.workfort.pstuian.ui.teacherprofile.state.TeacherProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TeacherProfileViewModel(
    private val userId: Int,
    private val teacherRepo: TeacherRepositoryImpl,
    private val authRepo: AuthRepository,
    private val uiStateMachine: TeacherProfileUiStateMachine,
) : UiStateMachineViewModel<TeacherProfileUiState>(
    uiStateMachine,
    initializationMode = InitializationMode.JustOnce,
) {

    private val _message = MutableStateFlow<TeacherProfileMessageState?>(null)
    val message: StateFlow<TeacherProfileMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<TeacherProfileNavigationState?>(null)
    val navigation: StateFlow<TeacherProfileNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {
        loadProfile()
    }

    fun onUiEvent(event: TeacherProfileUiEvent) {
        when (event) {
            is TeacherProfileUiEvent.LoadProfile -> loadProfile()
            is TeacherProfileUiEvent.BackClicked -> onClickBack()
            is TeacherProfileUiEvent.ImageClicked -> onClickImage(event.url)
            is TeacherProfileUiEvent.CallClicked -> onClickCall()
            is TeacherProfileUiEvent.EmailClicked -> onClickEmail()
            is TeacherProfileUiEvent.SignOutClicked -> onClickSignOut()
            is TeacherProfileUiEvent.TabClicked -> onClickTab(event.index)
            is TeacherProfileUiEvent.RefreshClicked -> onClickRefresh()
            is TeacherProfileUiEvent.ChangeImageClicked -> onClickChangeImage()
            is TeacherProfileUiEvent.EditBioClicked -> onClickEditBio()
            is TeacherProfileUiEvent.EditClicked -> onClickEdit(event.selectedTabIndex)
            is TeacherProfileUiEvent.ChangePasswordClicked -> onClickChangePassword()
            is TeacherProfileUiEvent.MyDeviceListClicked -> onClickMyDeviceList()
            is TeacherProfileUiEvent.DeleteAccountClicked -> onClickDeleteAccount()
            is TeacherProfileUiEvent.ChangeProfileImage -> changeProfileImage(event.imageUrl)
            is TeacherProfileUiEvent.ChangeBio -> changeBio(event.newBio)
            is TeacherProfileUiEvent.SignOut -> signOut()
        }
    }

    fun messageHandled() = _message.update { null }

    fun navigationHandled() = _navigation.update { null }

    private fun onClickBack() = _navigation.update { TeacherProfileNavigationState.GoBack }

    private fun onClickImage(url: String) {
        _navigation.update { TeacherProfileNavigationState.ImagePreviewScreen(url) }
    }

    private fun onClickCall() = profileCache()?.teacher?.phone?.let { phoneNumber ->
        _message.update {
            TeacherProfileMessageState.CallConfirmation(phoneNumber) {
                // Handle call in screen or via navigation
            }
        }
    }

    private fun onClickEmail() = profileCache()?.teacher?.email?.let { email ->
        _message.update {
            TeacherProfileMessageState.EmailConfirmation(email) {
                // Handle email in screen or via navigation
            }
        }
    }

    private fun onClickSignOut() {
        if (profileCache()?.isSignedIn == true) {
            _message.update {
                TeacherProfileMessageState.ConfirmSignOut {
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
        profileCache()?.teacher?.let { teacher ->
            _navigation.update {
                TeacherProfileNavigationState.ImageUploadScreen(
                    userId = teacher.id,
                    userType = UserType.TEACHER,
                )
            }
        }
    }

    private fun onClickEditBio() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.teacher?.let { teacher ->
            _message.update {
                TeacherProfileMessageState.InputBio(teacher.bio.orEmpty(), ::changeBio)
            }
        }
    }

    private fun onClickEdit(selectedTabIndex: Int) {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.teacher?.let { teacher ->
            when (selectedTabIndex) {
                0 -> ProfileEditMode.ACADEMIC
                1 -> ProfileEditMode.CONNECT
                else -> null
            }?.let { action ->
                _navigation.update {
                    TeacherProfileNavigationState.TeacherProfileEditScreen(
                        userId = teacher.id,
                        action = action,
                    )
                }
            }
        }
    }

    private fun onClickChangePassword() {
        if (profileCache()?.isSignedIn != true) return
        _navigation.update { TeacherProfileNavigationState.ChangePasswordScreen }
    }

    private fun onClickMyDeviceList() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.teacher?.let { teacher ->
            _navigation.update {
                TeacherProfileNavigationState.MyDeviceListScreen(
                    userId = teacher.id,
                    userType = UserType.TEACHER,
                )
            }
        }
    }

    private fun onClickDeleteAccount() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.teacher?.let { teacher ->
            _navigation.update {
                TeacherProfileNavigationState.DeleteAccountScreen(
                    userId = teacher.id,
                    userType = UserType.TEACHER,
                )
            }
        }
    }

    private fun profileCache(): TeacherProfile? {
        return when (val state = uiState.value.profileState) {
            is ProfileState.Available -> state.profile
            else -> null
        }
    }

    fun loadProfile() {
        getProfile(userId)
    }

    private fun getProfile(teacherId: Int) {
        uiStateMachine.showProfileLoading()
        viewModelScope.launch {
            runCatching {
                teacherRepo.getProfile(teacherId)
            }.onSuccess {
                uiStateMachine.showProfile(it)
            }.onFailure {
                val message = it.message ?: "Failed to load teacher profile"
                uiStateMachine.showProfileError(message)
            }
        }
    }

    private var isChangingPhoto = false
    fun changeProfileImage(imageUrl: String) {
        profileCache()?.let { cache ->
            if (isChangingPhoto || cache.isSignedIn.not()) {
                return
            }
            isChangingPhoto = true
            _message.update { TeacherProfileMessageState.Loading(cancelable = false) }
            viewModelScope.launch {
                runCatching {
                    teacherRepo.changeProfileImage(cache.teacher.toEntity(), imageUrl)
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
        val teacher = profileCache()?.teacher ?: return
        _message.update { TeacherProfileMessageState.Loading(cancelable = false) }
        viewModelScope.launch {
            runCatching {
                teacherRepo.changeBio(teacher.toEntity(), newBio)
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
        _message.update { TeacherProfileMessageState.Loading(cancelable = false) }
        viewModelScope.launch {
            runCatching {
                authRepo.signOut(fromAllDevice = false)
                messageHandled()
                loadProfile()
            }.onFailure {
                val message = it.message ?: "Signing out failed. Please try again."
                _message.update { TeacherProfileMessageState.Error(message) }
            }
        }
    }
}
