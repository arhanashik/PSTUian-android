package com.workfort.pstuian.ui.cvdownload.state

sealed interface CvDownloadUiState {
    data object None : CvDownloadUiState
    data class Content(
        val urlToDownload: String = "",
        val downloadFileName: String = "",
        val progress: Int = 0,
        val downloadResult: String? = null,
        val isDownloadSuccess: Boolean = false,
    ) : CvDownloadUiState
}
