package com.workfort.pstuian.ui.imageupload.state

sealed interface ImageUploadUiEvent {
    data object BackClicked : ImageUploadUiEvent
    data class ImageSelected(val uri: String) : ImageUploadUiEvent
    data class UploadClicked(val uri: String) : ImageUploadUiEvent
}
