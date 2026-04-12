package com.workfort.pstuian.app.ui.commonmodel.imageupload

import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.reducer.ui.imageupload.ImageUploadScreenState
import com.workfort.pstuian.reducer.ui.imageupload.ImageUploadScreenStateReducer
import com.workfort.pstuian.reducer.ui.imageupload.ImageUploadScreenStateUpdate
import com.workfort.pstuian.app.ui.commonmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ImageUploadViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val reducer: ImageUploadScreenStateReducer,
) : BaseViewModel() {

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

    fun onSelectImage(uri: String) {
        updateScreenState(ImageUploadScreenStateUpdate.ShowSelectedFile(uri))
        updateScreenState(ImageUploadScreenStateUpdate.ImageUploadStateNone)
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

    // Image upload logic is platform specific and should be handled in the UI layer
    // or through a platform-specific implementation.
    fun updateUploadProgress(progress: Int) {
        updateScreenState(ImageUploadScreenStateUpdate.ImageUploading(progress))
    }

    fun updateUploadResult(isSuccess: Boolean, result: String, url: String? = null) {
        updateScreenState(ImageUploadScreenStateUpdate.ImageUploadResult(isSuccess, result))
        if (isSuccess && url != null) {
            updateScreenState(
                ImageUploadScreenStateUpdate.NavigateTo(
                    ImageUploadScreenState.NavigationState.GoBack(url),
                ),
            )
        }
    }
}
