package com.workfort.pstuian.model
import kotlinx.serialization.Serializable





sealed class ProgressRequestState {
    data object Idle : ProgressRequestState()
    @Serializable
data class Loading(val progress: Int) : ProgressRequestState()
    @Serializable
data class Success<T>(val data: T? = null) : ProgressRequestState()
    @Serializable
data class Error(val error: String?) : ProgressRequestState()
}