package com.workfort.pstuian.ui.cvupload.state

sealed interface CvUploadMessageState {
    data class ConfirmUpload(val message: String, val onConfirm: () -> Unit) : CvUploadMessageState
    data class Loading(val cancelable: Boolean = false) : CvUploadMessageState
    data class Error(val message: String) : CvUploadMessageState
    data class Snackbar(val message: String) : CvUploadMessageState
}
