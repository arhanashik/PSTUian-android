package com.workfort.pstuian.reducer.ui.cvdownload

sealed interface CvDownloadScreenUiEvent {
    data object OnClickBack : CvDownloadScreenUiEvent
    data class OnDownload(val uri: String) : CvDownloadScreenUiEvent
    data object NavigationConsumed : CvDownloadScreenUiEvent
}
