package com.workfort.pstuian.app.ui.cvupload

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.ProgressRequestState
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.workmanager.usecase.domain.UploadPdfUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CvUploadViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val uploadPdfUseCase: UploadPdfUseCase,
    private val reducer: CvUploadScreenStateReducer,
) : ViewModel() {

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

    fun onSelectCv(context: Context, uri: Uri) {
        runCatching {
            val allowedFileSize = 2 * 1024 * 1024 // 2Mb
            val file = DocumentFile.fromSingleUri(context, uri) ?: throw Exception("Invalid file")
            if (file.length() > allowedFileSize) {
                throw Exception("File size too big! Allowed size is 2Mb.")
            } else {
                file.name ?: throw Exception("Invalid file")
            }
        }.onSuccess {
            cvUri = uri
            updateScreenState(CvUploadScreenStateUpdate.ShowSelectedFile(it))
            updateScreenState(CvUploadScreenStateUpdate.CvUploadStateNone)
        }.onFailure {
            cvUri = null
            updateScreenState(CvUploadScreenStateUpdate.ShowSelectedFile(""))
            updateScreenState(
                CvUploadScreenStateUpdate.CvUploadResult(
                    isSuccess = false,
                    it.message ?: "Invalid file",
                )
            )
        }
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

    private var cvUri: Uri? = null
    fun uploadCv(context: Context) {
        val uri = cvUri ?: return
        val fileName = "cv_${userType.type}_${userId}.pdf"
        viewModelScope.launch {
            uploadPdfUseCase(context, uri, fileName).collect { state ->
                when (state) {
                    is ProgressRequestState.Idle -> Unit
                    is ProgressRequestState.Loading -> {
                        updateScreenState(CvUploadScreenStateUpdate.CvUploading(state.progress))
                    }
                    is ProgressRequestState.Success<*> -> {
                        cvUri = null
                        updateScreenState(CvUploadScreenStateUpdate.ShowSelectedFile(""))
                        updateScreenState(
                            CvUploadScreenStateUpdate.CvUploadResult(
                                isSuccess = true,
                                result = state.data as String,
                            )
                        )
                    }
                    is ProgressRequestState.Error -> {
                        updateScreenState(
                            CvUploadScreenStateUpdate.CvUploadResult(
                                isSuccess = false,
                                result = state.error ?: "Failed to upload the cv. Please try again.",
                            )
                        )
                    }
                }
            }
        }
    }
}