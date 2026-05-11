package com.workfort.pstuian.ui.cvdownload.state

sealed interface CvDownloadUiEvent {
    data class OnSaveDestinationChosen(val destinationUri: String) : CvDownloadUiEvent
}
