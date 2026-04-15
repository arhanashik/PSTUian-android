package com.workfort.pstuian.ui.imageupload

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.imageupload.state.ImageUploadUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ImageUploadUiStateMachine : UiStateMachine<ImageUploadUiState> {

    private val _state = MutableStateFlow<ImageUploadUiState>(ImageUploadUiState.None)
    override val uiState: StateFlow<ImageUploadUiState> = _state.asStateFlow()

    private fun updateUiState(
        updater: ImageUploadUiState.() -> ImageUploadUiState,
    ) = _state.update(updater)

    fun setInitialContent() = updateUiState {
        ImageUploadUiState.Content()
    }

    fun onSelectImage(uri: String) = updateUiState {
        when (this) {
            is ImageUploadUiState.None -> this
            is ImageUploadUiState.Content -> copy(
                selectedFileUri = uri,
                uploadState = ImageUploadUiState.Content.ImageUploadState.None
            )
        }
    }

    fun onUploadProgress(progress: Int) = updateUiState {
        when (this) {
            is ImageUploadUiState.None -> this
            is ImageUploadUiState.Content -> copy(
                uploadState = ImageUploadUiState.Content.ImageUploadState.Uploading(progress)
            )
        }
    }

    fun onUploadResult(isSuccess: Boolean, result: String) = updateUiState {
        when (this) {
            is ImageUploadUiState.None -> this
            is ImageUploadUiState.Content -> copy(
                uploadState = if (isSuccess) {
                    ImageUploadUiState.Content.ImageUploadState.Success
                } else {
                    ImageUploadUiState.Content.ImageUploadState.Error(result)
                }
            )
        }
    }

    fun isUploading(): Boolean {
        val currentContent = _state.value as? ImageUploadUiState.Content ?: return false
        return currentContent.uploadState is ImageUploadUiState.Content.ImageUploadState.Uploading
    }
}
