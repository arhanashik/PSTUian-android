package com.workfort.pstuian.app.ui.commonmodel.cvupload

import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.reducer.ui.cvupload.CvUploadScreenState
import com.workfort.pstuian.reducer.ui.cvupload.CvUploadScreenStateReducer
import com.workfort.pstuian.reducer.ui.cvupload.CvUploadScreenStateUpdate
import com.workfort.pstuian.app.ui.commonmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CvUploadViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val reducer: CvUploadScreenStateReducer,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<CvUploadScreenState> get() = _screenState

    private fun updateScreenState(update: CvUploadScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(CvUploadScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(CvUploadScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        CvUploadScreenStateUpdate.NavigateTo(
            CvUploadScreenState.NavigationState.GoBack,
        ),
    )

    fun onSelectCv(uri: String, fileName: String) {
        updateScreenState(CvUploadScreenStateUpdate.ShowSelectedFile(uri, fileName))
        updateScreenState(CvUploadScreenStateUpdate.CvUploadStateNone)
    }

    fun onClickUpload() {
        val currentState = _screenState.value.displayState.cvUploadState
        if (currentState is CvUploadScreenState.DisplayState.CvUploadState.Uploading) {
            return
        }
        updateScreenState(
            CvUploadScreenStateUpdate.UpdateMessageState(
                CvUploadScreenState.DisplayState.MessageState.ConfirmUpload,
            ),
        )
    }

    // Cv upload logic is platform specific and should be handled in the UI layer
    // or through a platform-specific implementation.
    fun updateUploadProgress(progress: Int) {
        updateScreenState(CvUploadScreenStateUpdate.CvUploading(progress))
    }

    fun updateUploadResult(isSuccess: Boolean, result: String) {
        if (isSuccess) {
            updateScreenState(CvUploadScreenStateUpdate.ShowSelectedFile("", ""))
        }
        updateScreenState(CvUploadScreenStateUpdate.CvUploadResult(isSuccess, result))
    }

    fun updateMessageError(message: String) {
        updateScreenState(
            CvUploadScreenStateUpdate.UpdateMessageState(
                CvUploadScreenState.DisplayState.MessageState.Error(message)
            )
        )
    }
}
