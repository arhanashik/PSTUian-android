package com.workfort.pstuian.app.firebase

data class FcmMessageData(
    val title: String,
    val message: String,
    val type: String?,
    val action: String?,
)
