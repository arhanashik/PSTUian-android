package com.workfort.pstuian.firebase.fcm.callback

import com.workfort.pstuian.firebase.fcm.FcmMessageData

interface FcmCallback {
    fun onMessageReceived(data: FcmMessageData)
    fun onNewToken(token: String)
}