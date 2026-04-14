package com.workfort.pstuian.featuredomain.model
import kotlinx.serialization.SerialName

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse<T> (
    var success: Boolean,
    var code: Int? = -1,
    var message: String,
    var data: T? = null,
    @SerialName("auth_token")
    var authToken: String? = null
)