package com.workfort.pstuian.model

import com.google.gson.annotations.SerializedName

data class AuthResponse<T> (
    var success: Boolean,
    var code: Int? = -1,
    var message: String,
    var data: T? = null,
    @SerializedName("auth_token")
    var authToken: String? = null
)