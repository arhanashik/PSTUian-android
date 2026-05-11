package com.workfort.pstuian.ui.imageupload.state

sealed interface ImageUploadUiEvent {
    data object BackClicked : ImageUploadUiEvent
    data class ImageSelected(val fileUri: String) : ImageUploadUiEvent
    data class UploadClicked(val fileUri: String) : ImageUploadUiEvent
}
