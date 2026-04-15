package com.workfort.pstuian.ui.cvupload.state

sealed interface CvUploadUiEvent {
    data object BackClicked : CvUploadUiEvent
    data class CvSelected(val uri: String, val fileName: String) : CvUploadUiEvent
    data object UploadClicked : CvUploadUiEvent
    data object ConfirmUpload : CvUploadUiEvent
    data class UploadProgress(val progress: Int) : CvUploadUiEvent
    data class UploadResult(val isSuccess: Boolean, val result: String) : CvUploadUiEvent
}
