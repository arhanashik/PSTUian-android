package com.workfort.pstuian.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T> (
    private val code: String = "",
    val success: Boolean = false,
    val message: String = "",
    val data: T? = null,
) {
    val responseCode: ApiResponseCode
        get() = ApiResponseCode.create(code)

    val isSuccess: Boolean
        get() = responseCode.isSuccess()

    val isError: Boolean
        get() = responseCode.isError()
}