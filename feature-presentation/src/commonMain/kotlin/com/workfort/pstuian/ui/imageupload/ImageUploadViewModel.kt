package com.workfort.pstuian.ui.imageupload

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.AuthRepository
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
    private val authRepository: AuthRepository,
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
            is ImageUploadUiEvent.UploadClicked -> onClickUpload()
            is ImageUploadUiEvent.ConfirmUpload -> uploadImage()
            is ImageUploadUiEvent.UploadProgress -> onUploadProgress(event.progress)
            is ImageUploadUiEvent.UploadResult -> onUploadResult(event.isSuccess, event.result, event.url)
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

    private fun onClickUpload() {
        if (uiStateMachine.isUploading()) return
        _message.update { ImageUploadMessageState.ConfirmUpload }
    }

    private fun uploadImage() {
        val state = uiState.value as? ImageUploadUiState.Content ?: return
        val uri = state.selectedFileUri ?: return

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
//            authRepository.uploadProfileImage(
//                userId,
//                userType,
//                uri,
//                onProgress = { onUiEvent(ImageUploadUiEvent.UploadProgress(it)) },
//                onResult = { isSuccess, result, url ->
//                    onUiEvent(ImageUploadUiEvent.UploadResult(isSuccess, result, url))
//                }
//            )
        }
    }

    private fun onUploadProgress(progress: Int) {
        uiStateMachine.onUploadProgress(progress)
    }

    private fun onUploadResult(isSuccess: Boolean, result: String, url: String?) {
        uiStateMachine.onUploadResult(isSuccess, result)
        if (isSuccess) {
            _message.update { ImageUploadMessageState.Snackbar("Image uploaded successfully!") }
            _navigation.update { ImageUploadNavigationState.GoBack(url) }
        } else {
            _message.update { ImageUploadMessageState.Error(result) }
        }
    }
}
