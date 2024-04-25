package com.workfort.pstuian.app.ui.cvdownload


data class CvDownloadScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val urlToDownload: String,
        val downloadFileName: String,
        val cvDownloadState: CvDownloadState,
    ) {
        companion object {
            val INITIAL = DisplayState(
                urlToDownload = "",
                downloadFileName = "",
                cvDownloadState = CvDownloadState.None,
            )
        }
        sealed interface CvDownloadState {
            data object None : CvDownloadState
            data class Downloading(val progress: Int) : CvDownloadState
            data class Success(val url: String) : CvDownloadState
            data class Error(val message: String) : CvDownloadState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
    }
}