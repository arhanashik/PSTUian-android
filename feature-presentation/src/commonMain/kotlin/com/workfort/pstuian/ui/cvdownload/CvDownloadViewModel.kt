package com.workfort.pstuian.ui.cvdownload

import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadNavigationState
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadUiEvent
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class CvDownloadViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val urlToDownload: String,
    private val uiStateMachine: CvDownloadUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<CvDownloadUiState>(uiStateMachine) {

    private val _navigation = MutableStateFlow<CvDownloadNavigationState?>(null)
    val navigation: StateFlow<CvDownloadNavigationState?> = _navigation

    override fun onUiReady() {
        uiStateMachine.setInitialContent(
            urlToDownload = urlToDownload,
            downloadFileName = "cv_${userType.type}_${userId}.pdf",
        )
    }

    fun onUiEvent(event: CvDownloadUiEvent) {
        when (event) {
            is CvDownloadUiEvent.OnClickBack -> onClickBack()
            is CvDownloadUiEvent.OnUpdateDownloadProgress -> updateDownloadProgress(event.progress)
            is CvDownloadUiEvent.OnDownloadResult -> updateDownloadResult(event.isSuccess, event.result)
        }
    }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        _navigation.update { CvDownloadNavigationState.GoBack }
    }

    private fun updateDownloadProgress(progress: Int) {
        uiStateMachine.updateDownloadProgress(progress)
    }

    private fun updateDownloadResult(isSuccess: Boolean, result: String) {
        uiStateMachine.updateDownloadResult(isSuccess, result)
    }
}
