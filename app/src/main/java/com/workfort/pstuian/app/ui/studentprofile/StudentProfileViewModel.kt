package com.workfort.pstuian.app.ui.studentprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.ProfileEditMode
import com.workfort.pstuian.model.StudentProfile
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.repository.AuthRepository
import com.workfort.pstuian.repository.StudentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class StudentProfileViewModel(
    private val userId: Int,
    private val studentRepo: StudentRepository,
    private val authRepo: AuthRepository,
    private val stateReducer: StudentProfileScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(stateReducer.initial)
    val screenState: StateFlow<StudentProfileScreenState> get() = _screenState

    private fun updateScreenState(update: StudentProfileScreenStateUpdate) =
        _screenState.update { oldState -> stateReducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(StudentProfileScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(StudentProfileScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        StudentProfileScreenStateUpdate.NavigateTo(StudentProfileScreenState.NavigationState.GoBack)
    )

    fun onClickImage(url: String) {
        val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
        updateScreenState(
            StudentProfileScreenStateUpdate.NavigateTo(
                StudentProfileScreenState.NavigationState.ImagePreviewScreen(encodedUrl),
            )
        )
    }

    fun onClickCall() = profileCache()?.student?.phone?.let { phoneNumber ->
        updateScreenState(
            StudentProfileScreenStateUpdate.UpdateMessageState(
                StudentProfileScreenState.DisplayState.MessageState.Call(phoneNumber),
            ),
        )
    }

    fun onClickEmail() = profileCache()?.student?.email?.let { email ->
        updateScreenState(
            StudentProfileScreenStateUpdate.UpdateMessageState(
                StudentProfileScreenState.DisplayState.MessageState.Email(email),
            ),
        )
    }

    fun onClickSignOut() {
        if (profileCache()?.isSignedIn == true) {
            updateScreenState(
                StudentProfileScreenStateUpdate.UpdateMessageState(
                    StudentProfileScreenState.DisplayState.MessageState.ConfirmSignOut,
                ),
            )
        }
    }

    fun onClickTab(index: Int) {
        updateScreenState(StudentProfileScreenStateUpdate.UpdateSelectedTab(index))
    }

    fun onClickRefresh() = Unit

    fun onClickChangeImage() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.student?.let { student ->
            updateScreenState(
                StudentProfileScreenStateUpdate.NavigateTo(
                    StudentProfileScreenState.NavigationState.ImageUploadScreen(
                        userId = student.id,
                        userType = UserType.STUDENT,
                    )
                )
            )
        }
    }

    fun onClickEditBio() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.student?.let { student ->
            updateScreenState(
                StudentProfileScreenStateUpdate.UpdateMessageState(
                    StudentProfileScreenState.DisplayState.MessageState.InputBio(
                        student.bio.orEmpty(),
                    ),
                ),
            )
        }
    }

    fun onClickEdit(selectedTabIndex: Int) {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.student?.let { student ->
            when (selectedTabIndex) {
                0 -> ProfileEditMode.ACADEMIC
                1 -> ProfileEditMode.CONNECT
                else -> null
            }?.let { action ->
                updateScreenState(
                    StudentProfileScreenStateUpdate.NavigateTo(
                        StudentProfileScreenState.NavigationState.StudentProfileEditScreen(
                            userId = student.id,
                            action = action,
                        ),
                    ),
                )
            }
        }
    }

    fun onClickMyBloodDonationList() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.student?.let { student ->
            updateScreenState(
                StudentProfileScreenStateUpdate.NavigateTo(
                    StudentProfileScreenState.NavigationState.MyBloodDonationListScreen(
                        userId = student.id,
                        userType = UserType.STUDENT,
                    ),
                ),
            )
        }
    }

    fun onClickChangePassword() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.let {
            updateScreenState(
                StudentProfileScreenStateUpdate.NavigateTo(
                    StudentProfileScreenState.NavigationState.ChangePasswordScreen,
                ),
            )
        }
    }

    fun onClickDownloadCv(url: String) {
        profileCache()?.student?.let { student ->
            updateScreenState(
                StudentProfileScreenStateUpdate.NavigateTo(
                    StudentProfileScreenState.NavigationState.DownloadCvScreen(
                        userId = student.id,
                        userType = UserType.STUDENT,
                        url = url,
                    ),
                ),
            )
        }
    }

    fun onClickUploadCv() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.student?.let { student ->
            updateScreenState(
                StudentProfileScreenStateUpdate.NavigateTo(
                    StudentProfileScreenState.NavigationState.UploadCvScreen(
                        userId = student.id,
                        userType = UserType.STUDENT,
                    ),
                ),
            )
        }
    }

    fun onClickMyCheckInList() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.student?.let { student ->
            updateScreenState(
                StudentProfileScreenStateUpdate.NavigateTo(
                    StudentProfileScreenState.NavigationState.MyCheckInListScreen(
                        userId = student.id,
                        userType = UserType.STUDENT,
                    ),
                ),
            )
        }
    }

    fun onClickMyDeviceList() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.let {
            updateScreenState(
                StudentProfileScreenStateUpdate.NavigateTo(
                    StudentProfileScreenState.NavigationState.MyDeviceListScreen,
                ),
            )
        }
    }

    fun onClickDeleteAccount() {
        if (profileCache()?.isSignedIn != true) return
        profileCache()?.let {
            updateScreenState(
                StudentProfileScreenStateUpdate.NavigateTo(
                    StudentProfileScreenState.NavigationState.DeleteAccountScreen,
                ),
            )
        }
    }

    private fun profileCache(): StudentProfile? {
        return when (val state = _screenState.value.displayState.profileState) {
            is StudentProfileScreenState.DisplayState.ProfileState.Available -> state.profile
            else -> null
        }
    }

    fun loadProfile() {
        getProfile(userId)
    }

    private fun getProfile(studentId: Int) {
        updateScreenState(StudentProfileScreenStateUpdate.ProfileLoading)
        viewModelScope.launch {
            runCatching {
                studentRepo.getProfile(studentId)
            }.onSuccess {
                updateScreenState(StudentProfileScreenStateUpdate.ProfileLoaded(it))
                updateScreenState(
                    StudentProfileScreenStateUpdate.UpdateSignedInState(it.isSignedIn),
                )
            }.onFailure {
                val message = it.message ?: "Failed to load student profile"
                updateScreenState(StudentProfileScreenStateUpdate.ProfileLoadFailed(message))
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
                StudentProfileScreenStateUpdate.UpdateMessageState(
                    StudentProfileScreenState.DisplayState.MessageState.Loading(cancelable = false),
                ),
            )
            viewModelScope.launch {
                runCatching {
                    studentRepo.changeProfileImage(cache.student, imageUrl)
                }.onSuccess {
                    isChangingPhoto = false
                    updateScreenState(
                        StudentProfileScreenStateUpdate.UpdateMessageState(
                            StudentProfileScreenState.DisplayState.MessageState.Success(
                                "Profile photo changed successfully!"
                            ),
                        ),
                    )
                    loadProfile()
                }.onFailure {
                    isChangingPhoto = false
                    val message = it.message ?: "Failed to change photo. Please try again."
                    updateScreenState(
                        StudentProfileScreenStateUpdate.UpdateMessageState(
                            StudentProfileScreenState.DisplayState.MessageState.Error(message),
                        ),
                    )
                }
            }
        }
    }

    fun changeBio(newBio: String) {
        val student = profileCache()?.student ?: return
        updateScreenState(
            StudentProfileScreenStateUpdate.UpdateMessageState(
                StudentProfileScreenState.DisplayState.MessageState.Loading(cancelable = false),
            ),
        )
        viewModelScope.launch {
            runCatching {
                studentRepo.changeBio(student, newBio)
            }.onSuccess {
                val message = "Bio updated successfully"
                updateScreenState(
                    StudentProfileScreenStateUpdate.UpdateMessageState(
                        StudentProfileScreenState.DisplayState.MessageState.Success(message),
                    ),
                )
                loadProfile()
            }.onFailure {
                val message = it.message ?: "Failed to update bio. Please try again."
                updateScreenState(
                    StudentProfileScreenStateUpdate.UpdateMessageState(
                        StudentProfileScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }

    fun signOut() {
        updateScreenState(
            StudentProfileScreenStateUpdate.UpdateMessageState(
                StudentProfileScreenState.DisplayState.MessageState.Loading(cancelable = false),
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
                    StudentProfileScreenStateUpdate.UpdateMessageState(
                        StudentProfileScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }
}