package com.workfort.pstuian.ui.profile.studentprofile.state

sealed interface StudentProfileMessageState {
    /** Each open uses a new [openId] so the CV sheet gets a fresh ViewModel via Compose `key`. */
    data class CvDownloadSheet(
        val userId: Int,
        val url: String,
        val openId: Long,
        val onDismiss: (isSuccess: Boolean) -> Unit,
    ) : StudentProfileMessageState

    /** Each open uses a new [openId] so the CV sheet gets a fresh ViewModel via Compose `key`. */
    data class CvUploadSheet(
        val userId: Int,
        val openId: Long,
        val onDismiss: (isSuccess: Boolean) -> Unit,
    ) : StudentProfileMessageState

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
    data class Snackbar(val message: String) : StudentProfileMessageState
}