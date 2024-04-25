package com.workfort.pstuian.app.ui.cvupload


data class CvUploadScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val selectedFile: String,
        val cvUploadState: CvUploadState,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                selectedFile = "",
                cvUploadState = CvUploadState.None,
                messageState = null,
            )
        }
        sealed interface CvUploadState {
            data object None : CvUploadState
            data class Uploading(val progress: Int) : CvUploadState
            data class Success(val url: String) : CvUploadState
            data class Error(val message: String) : CvUploadState
        }
        sealed interface MessageState {
            data object ConfirmUpload : MessageState
            data class Error(val message: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
    }
}