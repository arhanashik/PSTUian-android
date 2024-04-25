package com.workfort.pstuian.app.ui.imageupload

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.ProgressRequestState
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.workmanager.usecase.domain.CompressImageUseCase
import com.workfort.pstuian.workmanager.usecase.domain.UploadImageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ImageUploadViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val compressImageUseCase: CompressImageUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val reducer: ImageUploadScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<ImageUploadScreenState> get() = _screenState

    private fun updateScreenState(update: ImageUploadScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(ImageUploadScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(ImageUploadScreenStateUpdate.NavigationConsumed)

    fun onClickBack() {
        if (isUploading()) {
            return
        }
        updateScreenState(
            ImageUploadScreenStateUpdate.NavigateTo(
                ImageUploadScreenState.NavigationState.GoBack(null),
            ),
        )
    }

    fun onSelectImage(uri: Uri) {
        imageUri = uri
        updateScreenState(ImageUploadScreenStateUpdate.ShowSelectedFile(uri))
        updateScreenState(ImageUploadScreenStateUpdate.CvUploadStateNone)
    }

    private fun isUploading(): Boolean {
        val currentState = _screenState.value.displayState.uploadState
        return currentState is ImageUploadScreenState.DisplayState.ImageUploadState.Uploading
    }

    fun onClickUpload() {
        if (isUploading()) {
            return
        }
        updateScreenState(
            ImageUploadScreenStateUpdate.UpdateMessageState(
                ImageUploadScreenState.DisplayState.MessageState.ConfirmUpload,
            ),
        )
    }

    private var imageUri: Uri? = null
    fun compressAndUploadImage(context: Context) {
        val uri = imageUri ?: return
        val fileName = "${userType.type}_${userId}_${System.currentTimeMillis()}.jpeg"
        viewModelScope.launch {
            compressImageUseCase(context, uri, fileName).collect { state ->
                when (state) {
                    is ProgressRequestState.Idle -> Unit
                    is ProgressRequestState.Loading -> {
                        updateScreenState(ImageUploadScreenStateUpdate.CvUploading(state.progress))
                    }
                    is ProgressRequestState.Success<*> -> {
                        uploadImage(context, fileName)
                    }
                    is ProgressRequestState.Error -> {
                        updateScreenState(
                            ImageUploadScreenStateUpdate.CvUploadResult(
                                isSuccess = false,
                                result = state.error ?: "Failed to compress. Please try again.",
                            )
                        )
                    }
                }
            }
        }
    }

    private fun uploadImage(context: Context, fileName: String) {
        viewModelScope.launch {
            uploadImageUseCase(context, userType, fileName).collect { state ->
                when (state) {
                    is ProgressRequestState.Idle -> Unit
                    is ProgressRequestState.Loading -> {
                        updateScreenState(ImageUploadScreenStateUpdate.CvUploading(state.progress))
                    }
                    is ProgressRequestState.Success<*> -> {
                        imageUri = null
                        val url = state.data as String
                        updateScreenState(
                            ImageUploadScreenStateUpdate.CvUploadResult(
                                isSuccess = true,
                                result = "Uploaded successfully!",
                            )
                        )
                        updateScreenState(
                            ImageUploadScreenStateUpdate.NavigateTo(
                                ImageUploadScreenState.NavigationState.GoBack(url),
                            ),
                        )
                    }
                    is ProgressRequestState.Error -> {
                        updateScreenState(
                            ImageUploadScreenStateUpdate.CvUploadResult(
                                isSuccess = false,
                                result = state.error ?: "Failed to upload. Please try again.",
                            )
                        )
                    }
                }
            }
        }
    }
}