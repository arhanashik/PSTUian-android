package com.workfort.pstuian.ui.imageupload.state

sealed interface ImageUploadNavigationState {
    data object GoBack : ImageUploadNavigationState
}
