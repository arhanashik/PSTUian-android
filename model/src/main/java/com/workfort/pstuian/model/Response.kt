package com.workfort.pstuian.model

data class Response<T> (
    var success: Boolean,
    var message: String,
    var data: T? = null
)