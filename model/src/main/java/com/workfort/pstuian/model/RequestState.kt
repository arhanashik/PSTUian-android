package com.workfort.pstuian.model
sealed class RequestState {
    data object Idle : RequestState()
    data object Loading : RequestState()
    data class Success<T>(val data: T? = null) : RequestState()
    data class Error(val error: String?) : RequestState()
}