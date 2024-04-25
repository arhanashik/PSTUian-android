package com.workfort.pstuian.app.ui.cvupload

import android.net.Uri

sealed class CvUploadScreenUiEvent {
    data object None : CvUploadScreenUiEvent()
    data class OnSelectCv(val uri: Uri) : CvUploadScreenUiEvent()
    data object OnClickBack : CvUploadScreenUiEvent()
    data object OnClickUpload : CvUploadScreenUiEvent()
    data object OnUpload : CvUploadScreenUiEvent()
    data object MessageConsumed : CvUploadScreenUiEvent()
    data object NavigationConsumed : CvUploadScreenUiEvent()
}