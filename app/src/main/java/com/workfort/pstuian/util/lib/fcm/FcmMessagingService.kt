package com.workfort.pstuian.util.lib.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
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
        Timber.e("From: ${remoteMessage.from}")
        AndroidUtil.vibrate()
        remoteMessage.data.let {
            Timber.e("Message data payload: $it")

//            val dataType = it[Const.FcmMessaging.DataKey.TYPE]
//            if(dataType == Const.FcmMessaging.DataType.MESSAGE) {
//                val msgId = it[Const.FcmMessaging.DataKey.MESSAGE_ID]?: ""
//                if(TextUtils.isDigitsOnly(msgId)) {
//                    ApiHelper(ApiService.createDeviceApiService()).updateMessageStatus(msgId.toInt())
//                }
//            }
        }

        remoteMessage.notification?.let {
            Timber.e("Message Notification Body: ${it.body}")
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