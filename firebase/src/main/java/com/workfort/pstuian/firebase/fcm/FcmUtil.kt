package com.workfort.pstuian.firebase.fcm

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

object FcmUtil {
    fun getFcmToken(onResponse: (token: String?, error: String?) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            val (token, error) = if (!task.isSuccessful) {
                null to (task.exception?.message ?: "Failed to get registration token")
            } else {
                // Get new FCM registration token
                val token = task.result
                if (token.isNullOrEmpty()) {
                    null to "Couldn't get registration token"
                } else {
                    token to null
                }
            }

            onResponse(token, error)
        }
    }

    fun subscribeToTopic(topic: String, onComplete: (success: Boolean) -> Unit) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun unsubscribeFromTopic(topic: String, onComplete: (success: Boolean) -> Unit) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }
}