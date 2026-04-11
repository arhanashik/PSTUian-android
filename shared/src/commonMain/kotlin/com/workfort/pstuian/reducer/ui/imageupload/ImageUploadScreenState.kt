package com.workfort.pstuian.reducer.ui.imageupload

data class ImageUploadScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val selectedFileUri: String?,
        val uploadState: ImageUploadState,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                selectedFileUri = null,
                uploadState = ImageUploadState.None,
                messageState = null,
            )
        }
        sealed interface ImageUploadState {
            data object None : ImageUploadState
            data class Uploading(val progress: Int) : ImageUploadState
            data class Success(val url: String) : ImageUploadState
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
