package com.workfort.pstuian.ui.imageupload.state

sealed interface ImageUploadMessageState {
    data object ConfirmUpload : ImageUploadMessageState
    data class Error(val message: String) : ImageUploadMessageState
    data class Snackbar(val message: String) : ImageUploadMessageState
}
