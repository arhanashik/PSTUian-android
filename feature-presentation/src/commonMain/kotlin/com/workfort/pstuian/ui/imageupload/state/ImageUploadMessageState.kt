package com.workfort.pstuian.ui.imageupload.state

sealed interface ImageUploadMessageState {
    data class ConfirmUpload(val message: String, val onConfirm: () -> Unit) : ImageUploadMessageState
    data class Loading(val cancelable: Boolean = false) : ImageUploadMessageState
    data class Error(val message: String) : ImageUploadMessageState
    data class Snackbar(val message: String) : ImageUploadMessageState
}
