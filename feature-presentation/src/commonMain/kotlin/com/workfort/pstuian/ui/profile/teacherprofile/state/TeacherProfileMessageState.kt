package com.workfort.pstuian.ui.profile.teacherprofile.state

sealed interface TeacherProfileMessageState {
    data class Loading(val cancelable: Boolean) : TeacherProfileMessageState
    data class InputBio(
        val currentBio: String,
        val onConfirm: (String) -> Unit,
    ) : TeacherProfileMessageState
    data class CallConfirmation(
        val phoneNumber: String,
        val onConfirm: () -> Unit,
    ) : TeacherProfileMessageState
    data class EmailConfirmation(
        val email: String,
        val onConfirm: () -> Unit,
    ) : TeacherProfileMessageState
    data class ConfirmSignOut(val onConfirm: () -> Unit) : TeacherProfileMessageState
    data class Success(val message: String) : TeacherProfileMessageState
    data class Error(val message: String) : TeacherProfileMessageState
}