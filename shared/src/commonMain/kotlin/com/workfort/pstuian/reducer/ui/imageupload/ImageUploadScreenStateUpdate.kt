package com.workfort.pstuian.reducer.ui.imageupload

import com.workfort.pstuian.reducer.service.StateUpdate

sealed interface ImageUploadScreenStateUpdate : StateUpdate<ImageUploadScreenState> {

    data class ShowSelectedFile(val uri: String) : ImageUploadScreenStateUpdate {
        override fun invoke(
            oldState: ImageUploadScreenState
        ): ImageUploadScreenState = with(oldState) {
            copy(displayState = displayState.copy(selectedFileUri = uri))
        }
    }

    data object ImageUploadStateNone : ImageUploadScreenStateUpdate {
        override fun invoke(
            oldState: ImageUploadScreenState
        ): ImageUploadScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    uploadState = ImageUploadScreenState.DisplayState.ImageUploadState.None,
                ),
            )
        }
    }

    data class ImageUploading(val progress: Int) : ImageUploadScreenStateUpdate {
        override fun invoke(
            oldState: ImageUploadScreenState
        ): ImageUploadScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    uploadState = ImageUploadScreenState.DisplayState.ImageUploadState.Uploading(
                        progress,
                    ),
                ),
            )
        }
    }

    data class ImageUploadResult(
        val isSuccess: Boolean,
        val result: String,
    ) : ImageUploadScreenStateUpdate {
        override fun invoke(
            oldState: ImageUploadScreenState
        ): ImageUploadScreenState = with(oldState) {
            val newState = if (isSuccess) {
                ImageUploadScreenState.DisplayState.ImageUploadState.Success(url = result)
            } else {
                ImageUploadScreenState.DisplayState.ImageUploadState.Error(message = result)
            }
            copy(displayState = displayState.copy(uploadState = newState))
        }
    }

    data class UpdateMessageState(
        val messageState: ImageUploadScreenState.DisplayState.MessageState
    ) : ImageUploadScreenStateUpdate {
        override fun invoke(
            oldState: ImageUploadScreenState
        ): ImageUploadScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : ImageUploadScreenStateUpdate {
        override fun invoke(
            oldState: ImageUploadScreenState
        ): ImageUploadScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: ImageUploadScreenState.NavigationState,
    ) : ImageUploadScreenStateUpdate {
        override fun invoke(
            oldState: ImageUploadScreenState,
        ): ImageUploadScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : ImageUploadScreenStateUpdate {
        override fun invoke(
            oldState: ImageUploadScreenState,
        ): ImageUploadScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}
