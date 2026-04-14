package com.workfort.pstuian.ui.cvupload

import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.cvupload.state.CvUploadMessageState
import com.workfort.pstuian.ui.cvupload.state.CvUploadNavigationState
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiEvent
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class CvUploadViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val uiStateMachine: CvUploadUiStateMachine,
) : UiStateMachineViewModel<CvUploadUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<CvUploadMessageState?>(null)
    val message: StateFlow<CvUploadMessageState?> = _message

    private val _navigation = MutableStateFlow<CvUploadNavigationState?>(null)
    val navigation: StateFlow<CvUploadNavigationState?> = _navigation

    override fun onUiReady() {
        uiStateMachine.setInitialContent()
    }

    fun onUiEvent(event: CvUploadUiEvent) {
        when (event) {
            is CvUploadUiEvent.OnClickBack -> onClickBack()
            is CvUploadUiEvent.OnSelectCv -> onSelectCv(event.uri, event.fileName)
            is CvUploadUiEvent.OnClickUpload -> onClickUpload()
            is CvUploadUiEvent.OnUpdateUploadProgress -> updateUploadProgress(event.progress)
            is CvUploadUiEvent.OnUploadResult -> updateUploadResult(event.isSuccess, event.result)
            is CvUploadUiEvent.OnError -> updateMessageError(event.message)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        _navigation.update { CvUploadNavigationState.GoBack }
    }

    private fun onSelectCv(uri: String, fileName: String) {
        uiStateMachine.setSelectedFile(uri, fileName)
    }

    private fun onClickUpload() {
        val state = uiState.value as? CvUploadUiState.Content ?: return
        if (state.progress in 1..99) {
            return
        }
        _message.update { CvUploadMessageState.ConfirmUpload }
    }

    private fun updateUploadProgress(progress: Int) {
        uiStateMachine.updateUploadProgress(progress)
    }

    private fun updateUploadResult(isSuccess: Boolean, result: String) {
        uiStateMachine.updateUploadResult(isSuccess, result)
        if (isSuccess) {
            _message.update { CvUploadMessageState.Success(result) }
        } else {
            _message.update { CvUploadMessageState.Error(result) }
        }
    }

    private fun updateMessageError(message: String) {
        _message.update { CvUploadMessageState.Error(message) }
    }
}
