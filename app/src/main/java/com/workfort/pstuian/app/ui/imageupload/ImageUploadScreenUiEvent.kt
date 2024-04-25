package com.workfort.pstuian.app.ui.imageupload

import android.net.Uri

sealed class ImageUploadScreenUiEvent {
    data object None : ImageUploadScreenUiEvent()
    data class OnSelectImage(val uri: Uri) : ImageUploadScreenUiEvent()
    data object OnClickBack : ImageUploadScreenUiEvent()
    data object OnClickUpload : ImageUploadScreenUiEvent()
    data object OnUpload : ImageUploadScreenUiEvent()
    data object MessageConsumed : ImageUploadScreenUiEvent()
    data object NavigationConsumed : ImageUploadScreenUiEvent()
}