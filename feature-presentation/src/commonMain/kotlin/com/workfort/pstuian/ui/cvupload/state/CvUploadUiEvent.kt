package com.workfort.pstuian.ui.cvupload.state

sealed interface CvUploadUiEvent {
    data object BackClicked : CvUploadUiEvent
    data class CvSelected(val fileUri: String) : CvUploadUiEvent
    data class UploadClicked(val fileUri: String) : CvUploadUiEvent
}
