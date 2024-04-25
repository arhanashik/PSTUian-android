package com.workfort.pstuian.app.ui.cvdownload

import com.workfort.pstuian.view.service.StateUpdate


sealed interface CvDownloadScreenStateUpdate : StateUpdate<CvDownloadScreenState> {

    data class ShowDownloadUrlAndFileName(
        val urlToDownload: String,
        val downloadFileName: String,
    ) : CvDownloadScreenStateUpdate {
        override fun invoke(
            oldState: CvDownloadScreenState
        ): CvDownloadScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    urlToDownload = urlToDownload,
                    downloadFileName = downloadFileName,
                ),
            )
        }
    }

    data class CvDownloading(val progress: Int) : CvDownloadScreenStateUpdate {
        override fun invoke(
            oldState: CvDownloadScreenState
        ): CvDownloadScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    cvDownloadState = CvDownloadScreenState.DisplayState.CvDownloadState.Downloading(
                        progress,
                    ),
                ),
            )
        }
    }

    data class CvDownloadResult(
        val isSuccess: Boolean,
        val result: String,
    ) : CvDownloadScreenStateUpdate {
        override fun invoke(
            oldState: CvDownloadScreenState
        ): CvDownloadScreenState = with(oldState) {
            val newState = if (isSuccess) {
                CvDownloadScreenState.DisplayState.CvDownloadState.Success(url = result)
            } else {
                CvDownloadScreenState.DisplayState.CvDownloadState.Error(message = result)
            }
            copy(displayState = displayState.copy(cvDownloadState = newState))
        }
    }

    data class NavigateTo(
        val newState: CvDownloadScreenState.NavigationState,
    ) : CvDownloadScreenStateUpdate {
        override fun invoke(
            oldState: CvDownloadScreenState,
        ): CvDownloadScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : CvDownloadScreenStateUpdate {
        override fun invoke(
            oldState: CvDownloadScreenState,
        ): CvDownloadScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}