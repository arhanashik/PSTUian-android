package com.workfort.pstuian.ui.imageupload.state

sealed interface ImageUploadUiEvent {
    data object MessageConsumed : ImageUploadUiEvent
    data object NavigationConsumed : ImageUploadUiEvent
    data object OnClickBack : ImageUploadUiEvent
    data class OnSelectImage(val uri: String) : ImageUploadUiEvent
    data object OnClickUpload : ImageUploadUiEvent
    data object OnUpload : ImageUploadUiEvent
    data class OnUploadProgress(val progress: Int) : ImageUploadUiEvent
    data class OnUploadResult(val isSuccess: Boolean, val result: String, val url: String?) : ImageUploadUiEvent
}
