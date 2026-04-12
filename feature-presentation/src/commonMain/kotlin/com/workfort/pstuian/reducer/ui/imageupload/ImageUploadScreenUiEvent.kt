package com.workfort.pstuian.reducer.ui.imageupload

sealed interface ImageUploadScreenUiEvent {
    data object OnClickBack : ImageUploadScreenUiEvent
    data class OnSelectImage(val uri: String) : ImageUploadScreenUiEvent
    data object OnClickUpload : ImageUploadScreenUiEvent
    data object OnUpload : ImageUploadScreenUiEvent
    data object MessageConsumed : ImageUploadScreenUiEvent
    data object NavigationConsumed : ImageUploadScreenUiEvent
}
