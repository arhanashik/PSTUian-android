package com.workfort.pstuian.firebase.fcm

import com.google.firebase.messaging.FirebaseMessaging
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object FcmUtil {

    suspend fun getFcmToken(): String = suspendCoroutine { continuation ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful || task.result.isNullOrEmpty()) {
                val error = task.exception?.message ?: "Failed to get registration token"
                continuation.resumeWithException(Throwable(message = error))
            } else {
                continuation.resume(task.result)
            }
        }
    }
}