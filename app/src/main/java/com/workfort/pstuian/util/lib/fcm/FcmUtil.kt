package com.workfort.pstuian.util.lib.fcm

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.workfort.pstuian.util.lib.fcm.callback.FcmTokenCallback
import com.workfort.pstuian.util.lib.fcm.callback.TopicSubscriptionCallback
import timber.log.Timber

/**
 *  ****************************************************************************
 *  * Created by : arhan on 28 Oct, 2021 at 13:23.
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

object FcmUtil {
    fun getFcmToken(callback: FcmTokenCallback? = null) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                callback?.onResponse(error = task.exception?.message
                    ?: "Failed to get registration token")
                Timber.e(task.exception, "Fetching FCM registration token failed")
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            if(token.isNullOrEmpty()) {
                callback?.onResponse(error = "Couldn't get registration token")
                return@OnCompleteListener
            }
            callback?.onResponse(token = token)
        })
    }

    fun subscribeToTopic(topic: String, callback: TopicSubscriptionCallback) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                callback.onComplete(task.isSuccessful)
            }
    }

    fun unsubscribeFromTopic(topic: String, callback: TopicSubscriptionCallback) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            .addOnCompleteListener { task ->
                callback.onComplete(task.isSuccessful)
            }
    }
}