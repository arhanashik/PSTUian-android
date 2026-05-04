package com.workfort.pstuian.ui.imageupload

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.FileHandlerRepository
import com.workfort.pstuian.platform.ImageToJpegEncoder
import com.workfort.pstuian.platform.UriBytesReader
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.imageupload.state.ImageUploadMessageState
import com.workfort.pstuian.ui.imageupload.state.ImageUploadNavigationState
import com.workfort.pstuian.ui.imageupload.state.ImageUploadUiEvent
import com.workfort.pstuian.ui.imageupload.state.ImageUploadUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ImageUploadViewModel(
    val userId: Int,
    val userType: UserType,
    private val fileHandlerRepository: FileHandlerRepository,
    private val uriBytesReader: UriBytesReader,
    private val imageToJpegEncoder: ImageToJpegEncoder,
    private val uiStateMachine: ImageUploadUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<ImageUploadUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<ImageUploadMessageState?>(null)
    val message: StateFlow<ImageUploadMessageState?> = _message

    private val _navigation = MutableStateFlow<ImageUploadNavigationState?>(null)
    val navigation: StateFlow<ImageUploadNavigationState?> = _navigation

    override fun onUiReady() {
        uiStateMachine.setInitialContent()
    }

    fun onUiEvent(event: ImageUploadUiEvent) {
        when (event) {
            is ImageUploadUiEvent.BackClicked -> onClickBack()
            is ImageUploadUiEvent.ImageSelected -> onImageSelected(event.uri)
            is ImageUploadUiEvent.UploadClicked -> onClickUpload(event.uri)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        if (uiStateMachine.isUploading()) return
        _navigation.update { ImageUploadNavigationState.GoBack(null) }
    }

    private fun onImageSelected(uri: String) {
        uiStateMachine.onSelectImage(uri)
    }

    private fun onClickUpload(uri: String) {
        if (uiStateMachine.isUploading()) return
        _message.update {
            ImageUploadMessageState.ConfirmUpload("This will replace your current profile photo") {
                uploadImage(uri)
            }
        }
    }

    private fun uploadImage(uri: String) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.onUploadProgress(0)

            val fileBytes = uriBytesReader.readBytes(uri).getOrElse { error ->
                val msg = error.message ?: "Could not read the selected image"
                _message.update { ImageUploadMessageState.Error(msg) }
                return@launchOnMain
            }

            val jpegBytes = imageToJpegEncoder.encodeToJpeg(fileBytes).getOrElse { error ->
                val msg = error.message ?: "Could not convert image to JPEG"
                _message.update { ImageUploadMessageState.Error(msg) }
                return@launchOnMain
            }

            uiStateMachine.onUploadProgress(50)
            val filename = "${userType.type}_${userId}.jpg"

            fileHandlerRepository.uploadImage(userType, filename, jpegBytes).onSuccess { url ->
                uiStateMachine.onUploadProgress(100)
                _message.update { ImageUploadMessageState.Snackbar("Image uploaded successfully!") }
                _navigation.update { ImageUploadNavigationState.GoBack(url) }
            }.onFailure {
                val msg = it.message ?: "Upload failed. Please try again."
                _message.update { ImageUploadMessageState.Error(msg) }
            }
        }
    }
}
