package com.workfort.pstuian.app.ui.cvdownload

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.ProgressRequestState
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.workmanager.usecase.domain.DownloadPdfUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CvDownloadViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val urlToDownload: String,
    private val downloadPdfUseCase: DownloadPdfUseCase,
    private val reducer: CvDownloadScreenStateReducer,
) : ViewModel() {

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

    fun downloadCv(context: Context, uri: Uri) {
        val currentState = _screenState.value.displayState.cvDownloadState
        if (currentState is CvDownloadScreenState.DisplayState.CvDownloadState.Downloading) {
            return
        }
        viewModelScope.launch {
            downloadPdfUseCase(context, urlToDownload, uri.toString()).collect { state ->
                when (state) {
                    is ProgressRequestState.Idle -> Unit
                    is ProgressRequestState.Loading -> {
                        updateScreenState(CvDownloadScreenStateUpdate.CvDownloading(state.progress))
                    }
                    is ProgressRequestState.Success<*> -> {
                        updateScreenState(
                            CvDownloadScreenStateUpdate.CvDownloadResult(
                                isSuccess = true,
                                result = state.data as String,
                            )
                        )
                    }
                    is ProgressRequestState.Error -> {
                        updateScreenState(
                            CvDownloadScreenStateUpdate.CvDownloadResult(
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