package com.workfort.pstuian.ui.teacherprofile.state

import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.TeacherProfile
import com.workfort.pstuian.featuredomain.model.UserType

data class TeacherProfileUiState(
    val profileState: ProfileState = ProfileState.None,
    val selectedTabIndex: Int = 0,
    val isSignedIn: Boolean = false,
    val messageState: MessageState? = null,
    val navigationState: NavigationState? = null,
)

sealed interface ProfileState {
    data object None : ProfileState
    data object Loading : ProfileState
    data class Available(val profile: TeacherProfile) : ProfileState
    data class Error(val message: String) : ProfileState
}

sealed interface MessageState {
    data class Loading(val cancelable: Boolean) : MessageState
    data class Success(val message: String) : MessageState
    data class Error(val message: String) : MessageState
    data class InputBio(val currentBio: String) : MessageState
    data class Call(val phoneNumber: String) : MessageState
    data class Email(val email: String) : MessageState
    data object ConfirmSignOut : MessageState
}

sealed interface NavigationState {
    data object GoBack : NavigationState
    data class ImagePreviewScreen(val encodedImageUrl: String) : NavigationState
    data class ImageUploadScreen(val userId: Int, val userType: UserType) : NavigationState
    data object ChangePasswordScreen : NavigationState
    data object MyDeviceListScreen : NavigationState
    data class TeacherProfileEditScreen(val userId: Int, val action: ProfileEditMode) : NavigationState
    data object DeleteAccountScreen : NavigationState
}
