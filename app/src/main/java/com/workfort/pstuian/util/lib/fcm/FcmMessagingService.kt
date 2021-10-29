package com.workfort.pstuian.util.lib.fcm

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.workfort.pstuian.PstuianApp
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.app.data.repository.AuthRepository
import com.workfort.pstuian.util.helper.AndroidUtil
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import timber.log.Timber

/**
 *  ****************************************************************************
 *  * Created by : arhan on 28 Oct, 2021 at 13:11.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/10/28.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class FcmMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Timber.e("Notification From: ${remoteMessage.from}")

        // update prefs to add new notification state
        Prefs.hasNewNotification = true

        AndroidUtil.vibrate()

        // notification data
        var action : String? = null
        remoteMessage.notification?.let {
            Timber.e("Notification body: ${it.body}")
            action = it.clickAction
        }
        remoteMessage.data.let {
            Timber.e("Notification payload: $it")

            val type = it[Const.Fcm.DataKey.TYPE]
            val title = it[Const.Fcm.DataKey.TITLE]
            val message = it[Const.Fcm.DataKey.MESSAGE]
            val context = PstuianApp.getBaseApplicationContext()
            val broadcastManager = LocalBroadcastManager.getInstance(context)

            val intent = Intent(Const.IntentAction.NOTIFICATION).apply {
                putExtra(Const.Fcm.DataKey.TYPE, type)
                putExtra(Const.Fcm.DataKey.TITLE, title)
                putExtra(Const.Fcm.DataKey.MESSAGE, message)
                putExtra(Const.Fcm.DataKey.ACTION, action)
            }
            broadcastManager.sendBroadcast(intent)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.e("Refreshed token: $token")
        val repo by inject<AuthRepository>()
        runBlocking {
            try {
                repo.updateFcmToken(token)
            }catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }
}