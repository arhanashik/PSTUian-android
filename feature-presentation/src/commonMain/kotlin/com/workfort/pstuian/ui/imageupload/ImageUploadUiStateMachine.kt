package com.workfort.pstuian.ui.imageupload

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.imageupload.state.ImageUploadUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ImageUploadUiStateMachine : UiStateMachine<ImageUploadUiState> {
    private val _uiState = MutableStateFlow(ImageUploadUiState())
    override val uiState: StateFlow<ImageUploadUiState> = _uiState

    fun messageConsumed() {
        _uiState.update { it.copy(displayState = it.displayState.copy(messageState = null)) }
    }

    fun navigationConsumed() {
        _uiState.update { it.copy(navigationState = null) }
    }

    fun onClickBack() {
        if (isUploading()) return
        _uiState.update {
            it.copy(navigationState = ImageUploadUiState.NavigationState.GoBack(null))
        }
    }

    fun onSelectImage(uri: String) {
        _uiState.update {
            it.copy(
                displayState = it.displayState.copy(
                    selectedFileUri = uri,
                    uploadState = ImageUploadUiState.DisplayState.ImageUploadState.None
                )
            )
        }
    }

    fun onClickUpload() {
        if (isUploading()) return
        _uiState.update {
            it.copy(
                displayState = it.displayState.copy(
                    messageState = ImageUploadUiState.DisplayState.MessageState.ConfirmUpload
                )
            )
        }
    }

    fun onUploadProgress(progress: Int) {
        _uiState.update {
            it.copy(
                displayState = it.displayState.copy(
                    uploadState = ImageUploadUiState.DisplayState.ImageUploadState.Uploading(progress)
                )
            )
        }
    }

    fun onUploadResult(isSuccess: Boolean, result: String, url: String?) {
        _uiState.update {
            it.copy(
                displayState = it.displayState.copy(
                    uploadState = if (isSuccess) {
                        ImageUploadUiState.DisplayState.ImageUploadState.Success
                    } else {
                        ImageUploadUiState.DisplayState.ImageUploadState.Error(result)
                    }
                ),
                navigationState = if (isSuccess && url != null) {
                    ImageUploadUiState.NavigationState.GoBack(url)
                } else {
                    it.navigationState
                }
            )
        }
    }

    private fun isUploading(): Boolean {
        return _uiState.value.displayState.uploadState is ImageUploadUiState.DisplayState.ImageUploadState.Uploading
    }
}
