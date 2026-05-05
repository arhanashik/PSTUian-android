package com.workfort.pstuian.ui.cvdownload

import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadUiEvent
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadUiState

class CvDownloadViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val urlToDownload: String,
    private val uiStateMachine: CvDownloadUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<CvDownloadUiState>(uiStateMachine) {

    override fun onUiReady() {
        uiStateMachine.setInitialContent(
            urlToDownload = urlToDownload,
            downloadFileName = "cv_${userType.type}_${userId}.pdf",
        )
    }

    fun onUiEvent(event: CvDownloadUiEvent) {
        when (event) {
            is CvDownloadUiEvent.OnUpdateDownloadProgress -> updateDownloadProgress(event.progress)
            is CvDownloadUiEvent.OnDownloadResult -> updateDownloadResult(event.isSuccess, event.result)
        }
    }

    private fun updateDownloadProgress(progress: Int) {
        uiStateMachine.updateDownloadProgress(progress)
    }

    private fun updateDownloadResult(isSuccess: Boolean, result: String) {
        uiStateMachine.updateDownloadResult(isSuccess, result)
    }
}
