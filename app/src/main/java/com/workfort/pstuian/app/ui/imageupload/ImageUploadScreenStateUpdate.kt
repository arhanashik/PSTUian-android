package com.workfort.pstuian.app.ui.imageupload

import android.net.Uri
import com.workfort.pstuian.view.service.StateUpdate


sealed interface ImageUploadScreenStateUpdate : StateUpdate<ImageUploadScreenState> {

    data class ShowSelectedFile(val uri: Uri) : ImageUploadScreenStateUpdate {
        override fun invoke(
            oldState: ImageUploadScreenState
        ): ImageUploadScreenState = with(oldState) {
            copy(displayState = displayState.copy(selectedFile = uri))
        }
    }

    data object CvUploadStateNone : ImageUploadScreenStateUpdate {
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

    data class CvUploading(val progress: Int) : ImageUploadScreenStateUpdate {
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

    data class CvUploadResult(
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