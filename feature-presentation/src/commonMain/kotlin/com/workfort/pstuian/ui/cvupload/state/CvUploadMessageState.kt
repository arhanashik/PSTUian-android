package com.workfort.pstuian.ui.cvupload.state

sealed interface CvUploadMessageState {
    data object ConfirmUpload : CvUploadMessageState
    data class Error(val message: String) : CvUploadMessageState
    data class Success(val message: String) : CvUploadMessageState
}
