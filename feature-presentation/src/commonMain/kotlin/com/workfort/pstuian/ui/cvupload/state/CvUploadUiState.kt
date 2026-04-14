package com.workfort.pstuian.ui.cvupload.state

sealed interface CvUploadUiState {
    data object None : CvUploadUiState
    data class Content(
        val selectedFileUri: String = "",
        val selectedFileName: String = "",
        val progress: Int = 0,
        val uploadResult: String? = null,
        val isUploadSuccess: Boolean = false,
    ) : CvUploadUiState
}
