package com.workfort.pstuian.app.firebase

interface FcmCallback {
    fun onMessageReceived(data: FcmMessageData)
    fun onNewToken(token: String)
}