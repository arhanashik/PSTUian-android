package com.workfort.pstuian.featuredomain.model
import kotlinx.serialization.Serializable

sealed class RequestState {
    data object Idle : RequestState()
    data object Loading : RequestState()
    @Serializable
data class Success<T>(val data: T? = null) : RequestState()
    @Serializable
data class Error(val error: String?) : RequestState()
}