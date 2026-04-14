package com.workfort.pstuian.ui.studentprofile.state

import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.StudentProfile
import com.workfort.pstuian.featuredomain.model.UserType

data class StudentProfileUiState(
    val selectedTabIndex: Int = 0,
    val isSignedIn: Boolean = false,
    val profileState: ProfileState = ProfileState.None,
    val messageState: MessageState? = null,
    val navigationState: NavigationState? = null,
)

sealed interface ProfileState {
    data object None : ProfileState
    data object Loading : ProfileState
    data class Available(val profile: StudentProfile) : ProfileState
    data class Error(val message: String) : ProfileState
}

sealed interface MessageState {
    data class Loading(val cancelable: Boolean) : MessageState
    data class InputBio(val currentBio: String) : MessageState
    data class Call(val phoneNumber: String) : MessageState
    data class Email(val email: String) : MessageState
    data object ConfirmSignOut : MessageState
    data class Success(val message: String) : MessageState
    data class Error(val message: String) : MessageState
}

sealed interface NavigationState {
    data object GoBack : NavigationState
    data class ImageUploadScreen(
        val userId: Int,
        val userType: UserType,
    ) : NavigationState
    data object ChangePasswordScreen : NavigationState
    data class DownloadCvScreen(
        val userId: Int,
        val userType: UserType,
        val url: String,
    ) : NavigationState
    data class UploadCvScreen(
        val userId: Int,
        val userType: UserType,
    ) : NavigationState
    data class MyBloodDonationListScreen(
        val userId: Int,
        val userType: UserType,
    ) : NavigationState
    data class MyCheckInListScreen(
        val userId: Int,
        val userType: UserType,
    ) : NavigationState
    data object MyDeviceListScreen : NavigationState
    data class StudentProfileEditScreen(
        val userId: Int,
        val action: ProfileEditMode,
    ) : NavigationState
    data object DeleteAccountScreen : NavigationState
    data class ImagePreviewScreen(val encodedImageUrl: String) : NavigationState
}
