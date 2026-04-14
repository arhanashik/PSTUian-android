package com.workfort.pstuian.ui.cvupload

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class CvUploadUiStateMachine : UiStateMachine<CvUploadUiState> {

    private val _state = MutableStateFlow<CvUploadUiState>(CvUploadUiState.None)
    override val uiState: StateFlow<CvUploadUiState> = _state.asStateFlow()

    private fun updateUiState(
        updater: CvUploadUiState.() -> CvUploadUiState,
    ) = _state.update(updater)

    fun setInitialContent() = updateUiState {
        CvUploadUiState.Content()
    }

    fun setSelectedFile(uri: String, fileName: String) = updateUiState {
        when (this) {
            is CvUploadUiState.Content -> copy(
                selectedFileUri = uri,
                selectedFileName = fileName,
                progress = 0,
                uploadResult = null,
                isUploadSuccess = false,
            )
            else -> this
        }
    }

    fun updateUploadProgress(progress: Int) = updateUiState {
        when (this) {
            is CvUploadUiState.Content -> copy(progress = progress)
            else -> this
        }
    }

    fun updateUploadResult(isSuccess: Boolean, result: String) = updateUiState {
        when (this) {
            is CvUploadUiState.Content -> copy(
                selectedFileUri = if (isSuccess) "" else selectedFileUri,
                selectedFileName = if (isSuccess) "" else selectedFileName,
                isUploadSuccess = isSuccess,
                uploadResult = result,
            )
            else -> this
        }
    }
}
