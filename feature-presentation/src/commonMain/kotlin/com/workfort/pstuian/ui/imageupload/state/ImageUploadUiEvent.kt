package com.workfort.pstuian.ui.imageupload.state

sealed interface ImageUploadUiEvent {
    data object BackClicked : ImageUploadUiEvent
    data class ImageSelected(val uri: String) : ImageUploadUiEvent
    data object UploadClicked : ImageUploadUiEvent
    data object ConfirmUpload : ImageUploadUiEvent
    data class UploadProgress(val progress: Int) : ImageUploadUiEvent
    data class UploadResult(val isSuccess: Boolean, val result: String, val url: String?) : ImageUploadUiEvent
}
