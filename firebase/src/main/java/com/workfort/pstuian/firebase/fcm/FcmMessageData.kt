package com.workfort.pstuian.firebase.fcm


data class FcmMessageData(
    val title: String,
    val message: String,
    val type: String?,
    val action: String?,
)
