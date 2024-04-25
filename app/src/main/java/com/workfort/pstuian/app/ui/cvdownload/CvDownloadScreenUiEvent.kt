package com.workfort.pstuian.app.ui.cvdownload

import android.net.Uri

sealed class CvDownloadScreenUiEvent {
    data object None : CvDownloadScreenUiEvent()
    data object OnClickBack : CvDownloadScreenUiEvent()
    data class OnDownload(val uri: Uri) : CvDownloadScreenUiEvent()
    data object NavigationConsumed : CvDownloadScreenUiEvent()
}