package com.workfort.pstuian.ui.cvupload

import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CvUploadUiStateMachine : UiStateMachine<CvUploadUiState> {

    private val _state = MutableStateFlow<CvUploadUiState>(CvUploadUiState.None)
    override val uiState: StateFlow<CvUploadUiState> = _state.asStateFlow()

    private fun updateUiState(
        updater: CvUploadUiState.() -> CvUploadUiState,
    ) = _state.update(updater)

    fun setInitialContent() = updateUiState {
        CvUploadUiState.Content()
    }

    fun setSelectedFile(fileUri: String, fileName: String) = updateUiState {
        when (this) {
            is CvUploadUiState.Content -> copy(
                selectedFileUri = fileUri,
                selectedFileName = fileName,
                uploadState = CvUploadUiState.Content.CvUploadState.None,
            )
            else -> this
        }
    }

    fun onUploadProgress(progress: Int) = updateUiState {
        when (this) {
            is CvUploadUiState.None -> this
            is CvUploadUiState.Content -> copy(
                uploadState = CvUploadUiState.Content.CvUploadState.Uploading(progress),
            )
        }
    }

    fun onUploadResult(isSuccess: Boolean, result: String) = updateUiState {
        when (this) {
            is CvUploadUiState.None -> this
            is CvUploadUiState.Content -> copy(
                uploadState = if (isSuccess) {
                    CvUploadUiState.Content.CvUploadState.Success
                } else {
                    CvUploadUiState.Content.CvUploadState.Error(result)
                }
            )
        }
    }

    fun isUploading(): Boolean {
        val currentContent = _state.value as? CvUploadUiState.Content ?: return false
        return currentContent.uploadState is CvUploadUiState.Content.CvUploadState.Uploading
    }

    fun selectedFileDisplayName(): String =
        (_state.value as? CvUploadUiState.Content)?.selectedFileName.orEmpty()
}
