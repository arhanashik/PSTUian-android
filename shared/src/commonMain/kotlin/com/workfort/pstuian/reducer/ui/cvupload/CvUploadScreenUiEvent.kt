package com.workfort.pstuian.reducer.ui.cvupload

sealed interface CvUploadScreenUiEvent {
    data object OnClickBack : CvUploadScreenUiEvent
    data class OnSelectCv(val uri: String) : CvUploadScreenUiEvent
    data object OnClickUpload : CvUploadScreenUiEvent
    data object OnUpload : CvUploadScreenUiEvent
    data object MessageConsumed : CvUploadScreenUiEvent
    data object NavigationConsumed : CvUploadScreenUiEvent
}
