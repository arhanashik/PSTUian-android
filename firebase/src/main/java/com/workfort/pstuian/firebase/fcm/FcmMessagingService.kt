package com.workfort.pstuian.firebase.fcm

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.firebase.fcm.callback.FcmCallback
import org.koin.android.ext.android.inject

class FcmMessagingService: FirebaseMessagingService() {
    val callback by inject<FcmCallback>()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // notification data
        var action : String? = null
        remoteMessage.notification?.let {
            action = it.clickAction
        }
        remoteMessage.data.let {
            val type = it[Const.Fcm.DataKey.TYPE]
            val title = it[Const.Fcm.DataKey.TITLE]
            val message = it[Const.Fcm.DataKey.MESSAGE]

            val intent = Intent(Const.IntentAction.NOTIFICATION).apply {
                putExtra(Const.Fcm.DataKey.TYPE, type)
                putExtra(Const.Fcm.DataKey.TITLE, title)
                putExtra(Const.Fcm.DataKey.MESSAGE, message)
                putExtra(Const.Fcm.DataKey.ACTION, action)
            }
            callback.onMessageReceived(intent)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        callback.onNewToken(token)
    }
}