package com.workfort.pstuian.ui.imageupload.state

data class ImageUploadUiState(
    val displayState: DisplayState = DisplayState(),
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val selectedFileUri: String? = null,
        val uploadState: ImageUploadState = ImageUploadState.None,
        val messageState: MessageState? = null,
    ) {
        sealed interface ImageUploadState {
            data object None : ImageUploadState
            data class Uploading(val progress: Int) : ImageUploadState
            data object Success : ImageUploadState
            data class Error(val message: String) : ImageUploadState
        }

        sealed interface MessageState {
            data object ConfirmUpload : MessageState
            data class Error(val message: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data class GoBack(val url: String?) : NavigationState
    }
}
