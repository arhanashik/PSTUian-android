package com.workfort.pstuian.ui.cvupload.state

sealed interface CvUploadUiEvent {
    data object OnClickBack : CvUploadUiEvent
    data class OnSelectCv(val uri: String, val fileName: String) : CvUploadUiEvent
    data object OnClickUpload : CvUploadUiEvent
    data class OnUpdateUploadProgress(val progress: Int) : CvUploadUiEvent
    data class OnUploadResult(val isSuccess: Boolean, val result: String) : CvUploadUiEvent
    data class OnError(val message: String) : CvUploadUiEvent
}
