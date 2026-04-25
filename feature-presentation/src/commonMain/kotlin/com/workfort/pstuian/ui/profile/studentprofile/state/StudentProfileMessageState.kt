package com.workfort.pstuian.ui.profile.studentprofile.state

sealed interface StudentProfileMessageState {
    data class Loading(val cancelable: Boolean) : StudentProfileMessageState
    data class InputBio(
        val currentBio: String,
        val onConfirm: (String) -> Unit,
    ) : StudentProfileMessageState
    data class CallConfirmation(
        val phoneNumber: String,
        val onConfirm: () -> Unit,
    ) : StudentProfileMessageState
    data class EmailConfirmation(
        val email: String,
        val onConfirm: () -> Unit,
    ) : StudentProfileMessageState
    data class ConfirmSignOut(val onConfirm: () -> Unit) : StudentProfileMessageState
    data class Success(val message: String) : StudentProfileMessageState
    data class Error(val message: String) : StudentProfileMessageState
}