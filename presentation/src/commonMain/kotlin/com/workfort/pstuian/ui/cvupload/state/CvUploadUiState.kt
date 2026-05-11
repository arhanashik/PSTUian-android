package com.workfort.pstuian.ui.cvupload.state

sealed interface CvUploadUiState {
    data object None : CvUploadUiState

    data class Content(
        val selectedFileUri: String? = null,
        val selectedFileName: String = "",
        val uploadState: CvUploadState = CvUploadState.None,
    ) : CvUploadUiState {
        sealed interface CvUploadState {
            data object None : CvUploadState
            data class Uploading(val progress: Int) : CvUploadState
            data object Success : CvUploadState
            data class Error(val message: String) : CvUploadState
        }
    }
}
