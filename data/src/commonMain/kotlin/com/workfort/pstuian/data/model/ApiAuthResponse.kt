package com.workfort.pstuian.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiAuthResponse<T> (
    val code: String = "",
    val success: Boolean = false,
    val message: String = "",
    val data: T? = null,
    @SerialName("auth_token")
    var authToken: String? = null,
) {
    val responseCode: ApiResponseCode
        get() = ApiResponseCode.create(code)

    val isSuccess: Boolean
        get() = responseCode.isSuccess()

    val isError: Boolean
        get() = responseCode.isError()
}