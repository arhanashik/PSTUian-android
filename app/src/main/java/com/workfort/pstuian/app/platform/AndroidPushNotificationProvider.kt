package com.workfort.pstuian.app.platform

import com.google.firebase.messaging.FirebaseMessaging
import com.workfort.pstuian.util.PushNotificationProvider
import kotlinx.coroutines.tasks.await

class AndroidPushNotificationProvider : PushNotificationProvider {
    override suspend fun getPushToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (_: Exception) {
            null
        }
    }
}