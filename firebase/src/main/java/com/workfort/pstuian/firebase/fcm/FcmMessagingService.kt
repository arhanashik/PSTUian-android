package com.workfort.pstuian.firebase.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.workfort.pstuian.firebase.fcm.callback.FcmCallback
import org.koin.android.ext.android.inject

class FcmMessagingService: FirebaseMessagingService() {
    private val callback by inject<FcmCallback>()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // notification data
        var action : String? = null
        remoteMessage.notification?.let {
            action = it.clickAction
        }
        remoteMessage.data.let {
            val type = it[FcmKey.DataKey.TYPE]
            val title = it[FcmKey.DataKey.TITLE]
            val message = it[FcmKey.DataKey.MESSAGE]

            val data = FcmMessageData(
                title.orEmpty(),
                message.orEmpty(),
                type,
                action
            )
            callback.onMessageReceived(data)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        callback.onNewToken(token)
    }
}