package com.workfort.pstuian.ui.imageupload

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.FileHandlerRepository
import com.workfort.pstuian.featuredomain.repository.StudentRepository
import com.workfort.pstuian.featuredomain.repository.TeacherRepository
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
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository,
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
            is ImageUploadUiEvent.ImageSelected -> uiStateMachine.onSelectImage(event.fileUri)
            is ImageUploadUiEvent.UploadClicked -> onClickUpload(event.fileUri)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        if (uiStateMachine.isUploading()) return
        _navigation.update { ImageUploadNavigationState.GoBack }
    }

    private fun onClickUpload(fileUri: String) {
        if (uiStateMachine.isUploading()) return
        _message.update {
            ImageUploadMessageState.ConfirmUpload("Are you surely want to upload the photo?") {
                uploadImage(fileUri)
            }
        }
    }

    private fun uploadImage(fileUri: String) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.onUploadProgress(0)

            val fileBytes = uriBytesReader.readBytes(fileUri).getOrElse { error ->
                val msg = error.message ?: "Could not read the selected image"
                uiStateMachine.onUploadResult(isSuccess = false, result = msg)
                _message.update { ImageUploadMessageState.Error(msg) }
                return@launchOnMain
            }

            val jpegBytes = imageToJpegEncoder.encodeToJpeg(fileBytes).getOrElse { error ->
                val msg = error.message ?: "Could not convert image to JPEG"
                uiStateMachine.onUploadResult(isSuccess = false, result = msg)
                _message.update { ImageUploadMessageState.Error(msg) }
                return@launchOnMain
            }

            uiStateMachine.onUploadProgress(50)
            val filename = "${userType.type}_${userId}.jpg"

            fileHandlerRepository.uploadImage(userType, filename, jpegBytes).onSuccess { imageUrl ->
                uiStateMachine.onUploadProgress(100)
                uiStateMachine.onUploadResult(isSuccess = true, result = "Image uploaded successfully!")
                _message.update { ImageUploadMessageState.Snackbar("Image uploaded successfully!") }
                updateImageUrl(imageUrl)
            }.onFailure {
                val msg = it.message ?: "Upload failed. Please try again."
                uiStateMachine.onUploadResult(isSuccess = false, result = msg)
                _message.update { ImageUploadMessageState.Error(msg) }
            }
        }
    }

    private fun updateImageUrl(imageUrl: String) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            _message.update { ImageUploadMessageState.Loading() }
            when (userType) {
                UserType.STUDENT -> studentRepository.changeProfileImage(userId, imageUrl)
                UserType.TEACHER -> teacherRepository.changeProfileImage(userId, imageUrl)
                else -> return@launchOnMain
            }.onSuccess {
                _message.update { ImageUploadMessageState.Snackbar("Profile photo changed successfully!") }
                _navigation.update { ImageUploadNavigationState.GoBack }
            }.onFailure {
                val msg = it.message ?: "Failed. Please try again."
                _message.update { ImageUploadMessageState.Error(msg) }
            }
        }
    }
}
