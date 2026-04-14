package com.workfort.pstuian.ui.cvupload.state

sealed interface CvUploadNavigationState {
    data object GoBack : CvUploadNavigationState
}
