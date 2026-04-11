package com.workfort.pstuian.model
import kotlinx.serialization.Serializable






@Serializable
data class Response<T> (
    var success: Boolean,
    var message: String,
    var data: T? = null
)