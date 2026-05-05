package com.workfort.pstuian.ui.imageupload.state

sealed interface ImageUploadNavigationState {
    data class GoBack(val invalidateImageCache: Boolean = false) : ImageUploadNavigationState
}
