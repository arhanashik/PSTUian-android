package com.workfort.pstuian.ui.teacherprofile.state

sealed interface TeacherProfileUiEvent {
    data object LoadProfile : TeacherProfileUiEvent
    data object ClickBack : TeacherProfileUiEvent
    data class ClickImage(val url: String) : TeacherProfileUiEvent
    data object ClickCall : TeacherProfileUiEvent
    data object ClickEmail : TeacherProfileUiEvent
    data object ClickSignOut : TeacherProfileUiEvent
    data class ClickTab(val index: Int) : TeacherProfileUiEvent
    data object ClickRefresh : TeacherProfileUiEvent
    data object ClickChangeImage : TeacherProfileUiEvent
    data object ClickEditBio : TeacherProfileUiEvent
    data class ClickEdit(val selectedTabIndex: Int) : TeacherProfileUiEvent
    data object ClickChangePassword : TeacherProfileUiEvent
    data object ClickMyDeviceList : TeacherProfileUiEvent
    data object ClickDeleteAccount : TeacherProfileUiEvent
    data class ChangeProfileImage(val imageUrl: String) : TeacherProfileUiEvent
    data class ChangeBio(val newBio: String) : TeacherProfileUiEvent
    data object SignOut : TeacherProfileUiEvent
    data class OnCall(val phoneNumber: String) : TeacherProfileUiEvent
    data class OnEmail(val email: String) : TeacherProfileUiEvent
    data object MessageConsumed : TeacherProfileUiEvent
    data object NavigationConsumed : TeacherProfileUiEvent
}
