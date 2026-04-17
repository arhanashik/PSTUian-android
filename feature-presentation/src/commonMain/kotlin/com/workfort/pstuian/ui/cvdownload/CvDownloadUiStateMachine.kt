package com.workfort.pstuian.ui.cvdownload

import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CvDownloadUiStateMachine : UiStateMachine<CvDownloadUiState> {

    private val _state = MutableStateFlow<CvDownloadUiState>(CvDownloadUiState.None)
    override val uiState: StateFlow<CvDownloadUiState> = _state.asStateFlow()

    private fun updateUiState(
        updater: CvDownloadUiState.() -> CvDownloadUiState,
    ) = _state.update(updater)

    fun setInitialContent(urlToDownload: String, downloadFileName: String) = updateUiState {
        CvDownloadUiState.Content(
            urlToDownload = urlToDownload,
            downloadFileName = downloadFileName,
        )
    }

    fun updateDownloadProgress(progress: Int) = updateUiState {
        when (this) {
            is CvDownloadUiState.Content -> copy(progress = progress)
            else -> this
        }
    }

    fun updateDownloadResult(isSuccess: Boolean, result: String) = updateUiState {
        when (this) {
            is CvDownloadUiState.Content -> copy(
                isDownloadSuccess = isSuccess,
                downloadResult = result,
            )
            else -> this
        }
    }
}
