package com.workfort.pstuian.app.ui.commonmodel.cvdownload

import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.reducer.ui.cvdownload.CvDownloadScreenState
import com.workfort.pstuian.app.ui.commonmodel.BaseViewModel
import com.workfort.pstuian.reducer.ui.cvdownload.CvDownloadScreenStateReducer
import com.workfort.pstuian.reducer.ui.cvdownload.CvDownloadScreenStateUpdate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CvDownloadViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val urlToDownload: String,
    private val reducer: CvDownloadScreenStateReducer,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<CvDownloadScreenState> get() = _screenState

    private fun updateScreenState(update: CvDownloadScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun navigationConsumed() = updateScreenState(CvDownloadScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        CvDownloadScreenStateUpdate.NavigateTo(
            CvDownloadScreenState.NavigationState.GoBack,
        ),
    )

    fun loadInitialValue() {
        updateScreenState(
            CvDownloadScreenStateUpdate.ShowDownloadUrlAndFileName(
                urlToDownload = urlToDownload,
                downloadFileName = "cv_${userType.type}_${userId}.pdf",
            )
        )
    }

    // Cv download logic is platform specific and should be handled in the UI layer
    // or through a platform-specific implementation.
    fun updateDownloadProgress(progress: Int) {
        updateScreenState(CvDownloadScreenStateUpdate.CvDownloading(progress))
    }

    fun updateDownloadResult(isSuccess: Boolean, result: String) {
        updateScreenState(CvDownloadScreenStateUpdate.CvDownloadResult(isSuccess, result))
    }
}
