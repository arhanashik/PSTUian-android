package com.workfort.pstuian.app.ui.teacherprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.ProfileEditMode
import com.workfort.pstuian.model.TeacherProfile
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.repository.AuthRepository
import com.workfort.pstuian.repository.TeacherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class TeacherProfileViewModel(
    private val userId: Int,
    private val teacherRepo: TeacherRepository,
    private val authRepo: AuthRepository,
    private val stateReducer: TeacherProfileScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(stateReducer.initial)
    val screenState: StateFlow<TeacherProfileScreenState> get() = _screenState

    private fun updateScreenState(update: TeacherProfileScreenStateUpdate) =
        _screenState.update { oldState -> stateReducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(TeacherProfileScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(TeacherProfileScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        TeacherProfileScreenStateUpdate.NavigateTo(TeacherProfileScreenState.NavigationState.GoBack)
    )

    fun onClickImage(url: String) {
        val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
        updateScreenState(
            TeacherProfileScreenStateUpdate.NavigateTo(
                TeacherProfileScreenState.NavigationState.ImagePreviewScreen(encodedUrl),
            )
        )
    }

    fun onClickCall() = profileCache()?.teacher?.phone?.let { phoneNumber ->
        updateScreenState(
            TeacherProfileScreenStateUpdate.UpdateMessageState(
                TeacherProfileScreenState.DisplayState.MessageState.Call(phoneNumber)
            )
        )
    }

    fun onClickEmail() = profileCache()?.teacher?.email?.let { email ->
        updateScreenState(
            TeacherProfileScreenStateUpdate.UpdateMessageState(
                TeacherProfileScreenState.DisplayState.MessageState.Email(email),
            ),
        )
    }

    fun onClickSignOut() {
        if (profileCache()?.isSignedIn == true) {
            updateScreenState(
                TeacherProfileScreenStateUpdate.UpdateMessageState(
                    TeacherProfileScreenState.DisplayState.MessageState.ConfirmSignOut,
                ),
            )
        }
    }

    fun onClickTab(index: Int) {
        updateScreenState(TeacherProfileScreenStateUpdate.UpdateSelectedTab(index))
    }

    fun onClickRefresh() = Unit

    fun onClickChangeImage() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.teacher?.let { teacher ->
            updateScreenState(
                TeacherProfileScreenStateUpdate.NavigateTo(
                    TeacherProfileScreenState.NavigationState.ImageUploadScreen(
                        userId = teacher.id,
                        userType = UserType.TEACHER,
                    )
                )
            )
        }
    }

    fun onClickEditBio() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.teacher?.let { student ->
            updateScreenState(
                TeacherProfileScreenStateUpdate.UpdateMessageState(
                    TeacherProfileScreenState.DisplayState.MessageState.InputBio(
                        student.bio.orEmpty(),
                    ),
                ),
            )
        }
    }

    fun onClickEdit(selectedTabIndex: Int) = profileCache()?.teacher?.let { teacher ->
        when (selectedTabIndex) {
            0 -> ProfileEditMode.ACADEMIC
            1 -> ProfileEditMode.CONNECT
            else -> null
        }?.let { action ->
            updateScreenState(
                TeacherProfileScreenStateUpdate.NavigateTo(
                    TeacherProfileScreenState.NavigationState.TeacherProfileEditScreen(
                        userId = teacher.id,
                        action = action,
                    ),
                ),
            )
        }
    }

    fun onClickChangePassword() {
        profileCache()?.let {
            updateScreenState(
                TeacherProfileScreenStateUpdate.NavigateTo(
                    TeacherProfileScreenState.NavigationState.ChangePasswordScreen,
                ),
            )
        }
    }

    fun onClickMyDeviceList() {
        profileCache()?.let {
            updateScreenState(
                TeacherProfileScreenStateUpdate.NavigateTo(
                    TeacherProfileScreenState.NavigationState.MyDeviceListScreen,
                ),
            )
        }
    }

    fun onClickDeleteAccount() {
        profileCache()?.let {
            updateScreenState(
                TeacherProfileScreenStateUpdate.NavigateTo(
                    TeacherProfileScreenState.NavigationState.DeleteAccountScreen,
                ),
            )
        }
    }

    private fun profileCache(): TeacherProfile? {
        return when (val state = _screenState.value.displayState.profileState) {
            is TeacherProfileScreenState.DisplayState.ProfileState.Available -> state.profile
            else -> null
        }
    }

    fun loadProfile() {
        updateScreenState(TeacherProfileScreenStateUpdate.ProfileLoading)
        viewModelScope.launch {
            runCatching {
                teacherRepo.getProfile(userId)
            }.onSuccess {
                updateScreenState(TeacherProfileScreenStateUpdate.ProfileLoaded(it))
                updateScreenState(
                    TeacherProfileScreenStateUpdate.UpdateSignedInState(it.isSignedIn),
                )
            }.onFailure {
                val message = it.message ?: "Failed to load student profile"
                updateScreenState(TeacherProfileScreenStateUpdate.ProfileLoadFailed(message))
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
            updateScreenState(
                TeacherProfileScreenStateUpdate.UpdateMessageState(
                    TeacherProfileScreenState.DisplayState.MessageState.Loading(cancelable = false),
                ),
            )
            viewModelScope.launch {
                runCatching {
                    teacherRepo.changeProfileImage(cache.teacher, imageUrl)
                }.onSuccess {
                    isChangingPhoto = false
                    updateScreenState(
                        TeacherProfileScreenStateUpdate.UpdateMessageState(
                            TeacherProfileScreenState.DisplayState.MessageState.Success(
                                "Profile photo changed successfully!"
                            ),
                        ),
                    )
                    loadProfile()
                }.onFailure {
                    isChangingPhoto = false
                    val message = it.message ?: "Failed to change photo. Please try again."
                    updateScreenState(
                        TeacherProfileScreenStateUpdate.UpdateMessageState(
                            TeacherProfileScreenState.DisplayState.MessageState.Error(message),
                        ),
                    )
                }
            }
        }
    }

    fun changeBio(newBio: String) {
        val teacher = profileCache()?.teacher ?: return
        updateScreenState(
            TeacherProfileScreenStateUpdate.UpdateMessageState(
                TeacherProfileScreenState.DisplayState.MessageState.Loading(cancelable = false),
            ),
        )
        viewModelScope.launch {
            runCatching {
                teacherRepo.changeBio(teacher, newBio)
            }.onSuccess {
                val message = "Bio updated successfully"
                updateScreenState(
                    TeacherProfileScreenStateUpdate.UpdateMessageState(
                        TeacherProfileScreenState.DisplayState.MessageState.Success(message),
                    ),
                )
                loadProfile()
            }.onFailure {
                val message = it.message ?: "Failed to update bio. Please try again."
                updateScreenState(
                    TeacherProfileScreenStateUpdate.UpdateMessageState(
                        TeacherProfileScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }

    fun signOut() {
        updateScreenState(
            TeacherProfileScreenStateUpdate.UpdateMessageState(
                TeacherProfileScreenState.DisplayState.MessageState.Loading(cancelable = false),
            ),
        )
        viewModelScope.launch {
            runCatching {
                authRepo.signOut(fromAllDevice = false)
                messageConsumed()
                loadProfile()
            }.onFailure {
                val message = it.message ?: "Signing out failed. Please try again."
                updateScreenState(
                    TeacherProfileScreenStateUpdate.UpdateMessageState(
                        TeacherProfileScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }
}