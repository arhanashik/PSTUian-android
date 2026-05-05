package com.workfort.pstuian.ui.cvdownload.state

sealed interface CvDownloadMessageState {
    data class ConfirmDownload(val message: String, val onConfirm: () -> Unit) : CvDownloadMessageState
    data class Loading(val cancelable: Boolean = false) : CvDownloadMessageState
    data class Error(val message: String) : CvDownloadMessageState
}
