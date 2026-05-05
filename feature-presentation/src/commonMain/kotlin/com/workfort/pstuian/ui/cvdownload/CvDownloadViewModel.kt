package com.workfort.pstuian.ui.cvdownload

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.DomainError
import com.workfort.pstuian.featuredomain.model.DomainErrorCode
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.getOrElse
import com.workfort.pstuian.featuredomain.network.CvPdfRemoteFetcher
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadMessageState
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadUiEvent
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadUiState
import com.workfort.pstuian.util.FileUtil
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
    private val fileUtil: FileUtil,
    private val cvPdfRemoteFetcher: CvPdfRemoteFetcher,
    private val uiStateMachine: CvDownloadUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<CvDownloadUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<CvDownloadMessageState?>(null)
    val message: StateFlow<CvDownloadMessageState?> = _message

    private val _finishSuccess = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val finishSuccess: SharedFlow<Unit> = _finishSuccess.asSharedFlow()

    private var isDownloading = false

    override fun onUiReady() {
        uiStateMachine.setInitialContent(
            urlToDownload = urlToDownload,
            downloadFileName = "cv_${userType.type}_${userId}.pdf",
        )
    }

    fun onUiEvent(event: CvDownloadUiEvent) {
        when (event) {
            is CvDownloadUiEvent.OnSaveDestinationChosen ->
                startDownloadTo(event.destinationUri)
        }
    }

    fun onMessageHandled() = _message.update { null }

    private fun updateDownloadResult(isSuccess: Boolean, result: String) {
        uiStateMachine.updateDownloadResult(isSuccess, result)
        if (isSuccess) {
            _message.update { null }
            _finishSuccess.tryEmit(Unit)
        } else {
            _message.update { CvDownloadMessageState.Error(result) }
        }
    }

    private fun startDownloadTo(destinationUri: String) {
        if (isDownloading) return
        if (urlToDownload.isBlank()) {
            failDownload("Nothing to download")
            return
        }

        isDownloading = true
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            try {
                uiStateMachine.clearDownloadOutcome()
                uiStateMachine.updateDownloadProgress(5)

                val bytes =
                    cvPdfRemoteFetcher.fetchPdfBytes(urlToDownload).getOrElse { err ->
                        failDownload(messageForFetchFailure(err))
                        return@launchOnMain
                    }

                uiStateMachine.updateDownloadProgress(55)

                val writeResult = fileUtil.writeBytes(destinationUri, bytes)
                if (writeResult.isFailure) {
                    val err = writeResult.exceptionOrNull()
                    failDownload(err?.message ?: "Could not save the CV")
                    return@launchOnMain
                }

                uiStateMachine.updateDownloadProgress(100)
                updateDownloadResult(isSuccess = true, result = DOWNLOAD_SUCCESS_MESSAGE)
            } finally {
                isDownloading = false
            }
        }
    }

    private fun failDownload(message: String) {
        uiStateMachine.updateDownloadProgress(0)
        updateDownloadResult(isSuccess = false, result = message)
    }

    private fun messageForFetchFailure(error: DomainError): String =
        when (error.code) {
            DomainErrorCode.Validation.InputInvalid -> INVALID_URL_MESSAGE
            DomainErrorCode.File.DownloadFailed -> error.message ?: DOWNLOAD_FAILED_MESSAGE
            else -> error.message ?: DOWNLOAD_FAILED_MESSAGE
        }

    companion object {
        private const val DOWNLOAD_SUCCESS_MESSAGE = "Cv downloaded successfully"
        private const val INVALID_URL_MESSAGE = "Invalid CV link"
        private const val DOWNLOAD_FAILED_MESSAGE = "Could not download the CV"
    }
}
