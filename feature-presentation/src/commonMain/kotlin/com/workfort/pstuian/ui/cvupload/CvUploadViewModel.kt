package com.workfort.pstuian.ui.cvupload

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.ui.cvupload.state.CvUploadMessageState
import com.workfort.pstuian.ui.cvupload.state.CvUploadNavigationState
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiEvent
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CvUploadViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val authRepository: AuthRepository,
    private val uiStateMachine: CvUploadUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
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
            is CvUploadUiEvent.BackClicked -> onClickBack()
            is CvUploadUiEvent.CvSelected -> onSelectCv(event.uri, event.fileName)
            is CvUploadUiEvent.UploadClicked -> onClickUpload()
            is CvUploadUiEvent.ConfirmUpload -> uploadCv()
            is CvUploadUiEvent.UploadProgress -> onUploadProgress(event.progress)
            is CvUploadUiEvent.UploadResult -> onUploadResult(event.isSuccess, event.result)
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
        _message.update { CvUploadMessageState.ConfirmUpload }
    }

    private fun uploadCv() {
        val state = uiState.value as? CvUploadUiState.Content ?: return
        val uri = state.selectedFileUri

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
//            authRepository.uploadCv(
//                userId,
//                userType,
//                uri,
//                onProgress = { onUiEvent(CvUploadUiEvent.UploadProgress(it)) },
//                onResult = { isSuccess, result ->
//                    onUiEvent(CvUploadUiEvent.UploadResult(isSuccess, result))
//                }
//            )
        }
    }

    private fun onUploadProgress(progress: Int) {
        uiStateMachine.updateUploadProgress(progress)
    }

    private fun onUploadResult(isSuccess: Boolean, result: String) {
        uiStateMachine.updateUploadResult(isSuccess, result)
        if (isSuccess) {
            _message.update { CvUploadMessageState.Snackbar(result) }
        } else {
            _message.update { CvUploadMessageState.Error(result) }
        }
    }
}
