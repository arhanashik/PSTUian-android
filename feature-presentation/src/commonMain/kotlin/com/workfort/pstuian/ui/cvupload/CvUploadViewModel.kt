package com.workfort.pstuian.ui.cvupload

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.FileHandlerRepository
import com.workfort.pstuian.featuredomain.repository.StudentRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.cvupload.state.CvUploadMessageState
import com.workfort.pstuian.ui.cvupload.state.CvUploadNavigationState
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiEvent
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiState
import com.workfort.pstuian.util.FileUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CvUploadViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val studentRepository: StudentRepository,
    private val fileHandlerRepository: FileHandlerRepository,
    private val fileUtil: FileUtil,
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
            is CvUploadUiEvent.CvSelected -> onSelectCv(event.fileUri)
            is CvUploadUiEvent.UploadClicked -> onClickUpload(event.fileUri)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        if (uiStateMachine.isUploading()) return
        _navigation.update { CvUploadNavigationState.GoBack }
    }

    private fun onSelectCv(fileUri: String) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            val fileName = fileUtil.getFileName(fileUri).getOrElse { FileUtil.FALLBACK_DOCUMENT_NAME }
            uiStateMachine.setSelectedFile(fileUri, fileName)
        }
    }

    private fun onClickUpload(fileUri: String) {
        if (uiStateMachine.isUploading()) return
        _message.update {
            CvUploadMessageState.ConfirmUpload("Are you surely want to upload the cv?") {
                uploadCv(fileUri)
            }
        }
    }

    private fun uploadCv(fileUri: String) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.onUploadProgress(0)

            val fileBytes = fileUtil.readBytes(fileUri).getOrElse { error ->
                val msg = error.message ?: "Could not read the selected file"
                uiStateMachine.onUploadResult(isSuccess = false, result = msg)
                _message.update { CvUploadMessageState.Error(msg) }
                return@launchOnMain
            }

            uiStateMachine.onUploadProgress(50)
            val filename = "cv_${userType.type}_$userId.pdf"

            fileHandlerRepository.uploadCv(filename, fileBytes).onSuccess { fileUrl ->
                uiStateMachine.onUploadProgress(100)
                uiStateMachine.onUploadResult(isSuccess = true, result = "Image uploaded successfully!")
                _message.update { CvUploadMessageState.Snackbar("Image uploaded successfully!") }
                updateFileUrl(fileUrl)
            }.onFailure {
                val msg = it.message ?: "Upload failed. Please try again."
                uiStateMachine.onUploadResult(isSuccess = false, result = msg)
                _message.update { CvUploadMessageState.Error(msg) }
            }
        }
    }

    private fun updateFileUrl(fileUrl: String) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            _message.update { CvUploadMessageState.Loading() }
            when (userType) {
                UserType.STUDENT -> studentRepository.changeCvUrl(userId, fileUrl)
                else -> return@launchOnMain
            }.onSuccess {
                _message.update { CvUploadMessageState.Snackbar("Profile photo changed successfully!") }
                _navigation.update { CvUploadNavigationState.GoBack }
            }.onFailure {
                val msg = it.message ?: "Failed. Please try again."
                _message.update { CvUploadMessageState.Error(msg) }
            }
        }
    }
}
