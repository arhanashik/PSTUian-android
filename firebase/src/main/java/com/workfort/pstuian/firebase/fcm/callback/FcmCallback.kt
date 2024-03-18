package com.workfort.pstuian.firebase.fcm.callback

import android.content.Intent

interface FcmCallback {
    fun onMessageReceived(data: Intent)
    fun onNewToken(token: String)
}