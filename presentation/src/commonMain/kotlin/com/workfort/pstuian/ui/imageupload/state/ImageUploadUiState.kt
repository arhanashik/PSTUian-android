package com.workfort.pstuian.ui.imageupload.state

sealed interface ImageUploadUiState {
    data object None : ImageUploadUiState

    data class Content(
        val selectedFileUri: String? = null,
        val uploadState: ImageUploadState = ImageUploadState.None,
    ) : ImageUploadUiState {
        sealed interface ImageUploadState {
            data object None : ImageUploadState
            data class Uploading(val progress: Int) : ImageUploadState
            data object Success : ImageUploadState
            data class Error(val message: String) : ImageUploadState
        }
    }
}
