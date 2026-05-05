package com.workfort.pstuian.ui.cvdownload

import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadMessageState
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadUiEvent
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update

class CvDownloadViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val urlToDownload: String,
    private val uiStateMachine: CvDownloadUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<CvDownloadUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<CvDownloadMessageState?>(null)
    val message: StateFlow<CvDownloadMessageState?> = _message

    private val _finishSuccess = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val finishSuccess: SharedFlow<Unit> = _finishSuccess.asSharedFlow()

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

    fun onMessageHandled() = _message.update { null }

    private fun updateDownloadProgress(progress: Int) {
        uiStateMachine.updateDownloadProgress(progress)
    }

    private fun updateDownloadResult(isSuccess: Boolean, result: String) {
        uiStateMachine.updateDownloadResult(isSuccess, result)
        if (isSuccess) {
            _message.update { null }
            _finishSuccess.tryEmit(Unit)
        } else {
            _message.update { CvDownloadMessageState.Error(result) }
        }
    }
}
