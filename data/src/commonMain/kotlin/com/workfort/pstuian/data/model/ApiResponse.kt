package com.workfort.pstuian.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T> (
    val code: String = "",
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