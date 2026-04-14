package com.workfort.pstuian.ui.teacherprofile

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.InitializationMode
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.data.infrastructure.repository.TeacherRepositoryImpl
import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.TeacherProfile
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.ui.teacherprofile.state.MessageState
import com.workfort.pstuian.ui.teacherprofile.state.NavigationState
import com.workfort.pstuian.ui.teacherprofile.state.ProfileState
import com.workfort.pstuian.ui.teacherprofile.state.TeacherProfileUiState
import kotlinx.coroutines.launch

internal class TeacherProfileViewModel(
    private val userId: Int,
    private val teacherRepo: TeacherRepositoryImpl,
    private val authRepo: AuthRepository,
    private val uiStateMachine: TeacherProfileUiStateMachine,
) : UiStateMachineViewModel<TeacherProfileUiState>(
    uiStateMachine,
    initializationMode = InitializationMode.JustOnce,
) {

    override fun onUiReady() {
        loadProfile()
    }

    fun messageConsumed() = uiStateMachine.showMessage(null)

    fun navigationConsumed() = uiStateMachine.navigateTo(null)

    fun onClickBack() = uiStateMachine.navigateTo(NavigationState.GoBack)

    fun onClickImage(url: String) {
        uiStateMachine.navigateTo(NavigationState.ImagePreviewScreen(url))
    }

    fun onClickCall() = profileCache()?.teacher?.phone?.let { phoneNumber ->
        uiStateMachine.showMessage(MessageState.Call(phoneNumber))
    }

    fun onClickEmail() = profileCache()?.teacher?.email?.let { email ->
        uiStateMachine.showMessage(MessageState.Email(email))
    }

    fun onClickSignOut() {
        if (profileCache()?.isSignedIn == true) {
            uiStateMachine.showMessage(MessageState.ConfirmSignOut)
        }
    }

    fun onClickTab(index: Int) {
        uiStateMachine.updateSelectedTab(index)
    }

    fun onClickRefresh() = loadProfile()

    fun onClickChangeImage() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.teacher?.let { teacher ->
            uiStateMachine.navigateTo(
                NavigationState.ImageUploadScreen(
                    userId = teacher.id,
                    userType = UserType.TEACHER,
                )
            )
        }
    }

    fun onClickEditBio() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.teacher?.let { teacher ->
            uiStateMachine.showMessage(MessageState.InputBio(teacher.bio.orEmpty()))
        }
    }

    fun onClickEdit(selectedTabIndex: Int) {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.teacher?.let { teacher ->
            when (selectedTabIndex) {
                0 -> ProfileEditMode.ACADEMIC
                1 -> ProfileEditMode.CONNECT
                else -> null
            }?.let { action ->
                uiStateMachine.navigateTo(
                    NavigationState.TeacherProfileEditScreen(
                        userId = teacher.id,
                        action = action,
                    )
                )
            }
        }
    }

    fun onClickChangePassword() {
        if (profileCache()?.isSignedIn != true) return
        uiStateMachine.navigateTo(NavigationState.ChangePasswordScreen)
    }

    fun onClickMyDeviceList() {
        if (profileCache()?.isSignedIn != true) return
        uiStateMachine.navigateTo(NavigationState.MyDeviceListScreen)
    }

    fun onClickDeleteAccount() {
        if (profileCache()?.isSignedIn != true) return
        uiStateMachine.navigateTo(NavigationState.DeleteAccountScreen)
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
            uiStateMachine.showMessage(MessageState.Loading(cancelable = false))
            viewModelScope.launch {
                runCatching {
                    teacherRepo.changeProfileImage(cache.teacher.toEntity(), imageUrl)
                }.onSuccess {
                    isChangingPhoto = false
                    uiStateMachine.showMessage(
                        MessageState.Success("Profile photo changed successfully!")
                    )
                    loadProfile()
                }.onFailure {
                    isChangingPhoto = false
                    val message = it.message ?: "Failed to change photo. Please try again."
                    uiStateMachine.showMessage(MessageState.Error(message))
                }
            }
        }
    }

    fun changeBio(newBio: String) {
        val teacher = profileCache()?.teacher ?: return
        uiStateMachine.showMessage(MessageState.Loading(cancelable = false))
        viewModelScope.launch {
            runCatching {
                teacherRepo.changeBio(teacher.toEntity(), newBio)
            }.onSuccess {
                val message = "Bio updated successfully"
                uiStateMachine.showMessage(MessageState.Success(message))
                loadProfile()
            }.onFailure {
                val message = it.message ?: "Failed to update bio. Please try again."
                uiStateMachine.showMessage(MessageState.Error(message))
            }
        }
    }

    fun signOut() {
        uiStateMachine.showMessage(MessageState.Loading(cancelable = false))
        viewModelScope.launch {
            runCatching {
                authRepo.signOut(fromAllDevice = false)
                messageConsumed()
                loadProfile()
            }.onFailure {
                val message = it.message ?: "Signing out failed. Please try again."
                uiStateMachine.showMessage(MessageState.Error(message))
            }
        }
    }
}
