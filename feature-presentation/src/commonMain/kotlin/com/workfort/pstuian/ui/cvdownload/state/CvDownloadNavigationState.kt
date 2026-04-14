package com.workfort.pstuian.ui.cvdownload.state

sealed interface CvDownloadNavigationState {
    data object GoBack : CvDownloadNavigationState
}
