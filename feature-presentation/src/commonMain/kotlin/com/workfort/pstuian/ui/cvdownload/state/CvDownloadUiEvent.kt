package com.workfort.pstuian.ui.cvdownload.state

sealed interface CvDownloadUiEvent {
    data class OnUpdateDownloadProgress(val progress: Int) : CvDownloadUiEvent
    data class OnDownloadResult(val isSuccess: Boolean, val result: String) : CvDownloadUiEvent
}
