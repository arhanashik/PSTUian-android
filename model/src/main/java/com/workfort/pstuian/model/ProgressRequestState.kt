package com.workfort.pstuian.model
sealed class ProgressRequestState {
    data object Idle : ProgressRequestState()
    data class Loading(val progress: Int) : ProgressRequestState()
    data class Success<T>(val data: T? = null) : ProgressRequestState()
    data class Error(val error: String?) : ProgressRequestState()
}