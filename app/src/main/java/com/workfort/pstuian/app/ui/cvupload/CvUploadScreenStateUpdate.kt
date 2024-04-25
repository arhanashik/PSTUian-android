package com.workfort.pstuian.app.ui.cvupload

import com.workfort.pstuian.view.service.StateUpdate


sealed interface CvUploadScreenStateUpdate : StateUpdate<CvUploadScreenState> {

    data class ShowSelectedFile(val selectedFile: String) : CvUploadScreenStateUpdate {
        override fun invoke(
            oldState: CvUploadScreenState
        ): CvUploadScreenState = with(oldState) {
            copy(displayState = displayState.copy(selectedFile = selectedFile))
        }
    }

    data object CvUploadStateNone : CvUploadScreenStateUpdate {
        override fun invoke(
            oldState: CvUploadScreenState
        ): CvUploadScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    cvUploadState = CvUploadScreenState.DisplayState.CvUploadState.None,
                ),
            )
        }
    }

    data class CvUploading(val progress: Int) : CvUploadScreenStateUpdate {
        override fun invoke(
            oldState: CvUploadScreenState
        ): CvUploadScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    cvUploadState = CvUploadScreenState.DisplayState.CvUploadState.Uploading(
                        progress,
                    ),
                ),
            )
        }
    }

    data class CvUploadResult(
        val isSuccess: Boolean,
        val result: String,
    ) : CvUploadScreenStateUpdate {
        override fun invoke(
            oldState: CvUploadScreenState
        ): CvUploadScreenState = with(oldState) {
            val newState = if (isSuccess) {
                CvUploadScreenState.DisplayState.CvUploadState.Success(url = result)
            } else {
                CvUploadScreenState.DisplayState.CvUploadState.Error(message = result)
            }
            copy(displayState = displayState.copy(cvUploadState = newState))
        }
    }

    data class UpdateMessageState(
        val messageState: CvUploadScreenState.DisplayState.MessageState
    ) : CvUploadScreenStateUpdate {
        override fun invoke(
            oldState: CvUploadScreenState
        ): CvUploadScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : CvUploadScreenStateUpdate {
        override fun invoke(
            oldState: CvUploadScreenState
        ): CvUploadScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: CvUploadScreenState.NavigationState,
    ) : CvUploadScreenStateUpdate {
        override fun invoke(
            oldState: CvUploadScreenState,
        ): CvUploadScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : CvUploadScreenStateUpdate {
        override fun invoke(
            oldState: CvUploadScreenState,
        ): CvUploadScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}