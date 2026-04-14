package com.workfort.pstuian.app.firebase

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object FcmUtil {

    suspend fun getFcmToken(): String = suspendCancellableCoroutine { continuation ->
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