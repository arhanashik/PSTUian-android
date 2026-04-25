package com.workfort.pstuian.ui.profile.teacherprofile.state

sealed interface TeacherProfileUiEvent {
    data object LoadProfile : TeacherProfileUiEvent
    data object BackClicked : TeacherProfileUiEvent
    data class ImageClicked(val url: String) : TeacherProfileUiEvent
    data object CallClicked : TeacherProfileUiEvent
    data object EmailClicked : TeacherProfileUiEvent
    data object SignOutClicked : TeacherProfileUiEvent
    data class TabClicked(val index: Int) : TeacherProfileUiEvent
    data object RefreshClicked : TeacherProfileUiEvent
    data object ChangeImageClicked : TeacherProfileUiEvent
    data object EditBioClicked : TeacherProfileUiEvent
    data class EditClicked(val selectedTabIndex: Int) : TeacherProfileUiEvent
    data object ChangePasswordClicked : TeacherProfileUiEvent
    data object MyDeviceListClicked : TeacherProfileUiEvent
    data object DeleteAccountClicked : TeacherProfileUiEvent
    data class ChangeProfileImage(val imageUrl: String) : TeacherProfileUiEvent
    data class ChangeBio(val newBio: String) : TeacherProfileUiEvent
    data object SignOut : TeacherProfileUiEvent
}
