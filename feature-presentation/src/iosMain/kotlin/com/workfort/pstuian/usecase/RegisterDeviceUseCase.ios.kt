package com.workfort.pstuian.usecase

actual object FcmTokenProvider {
    actual suspend fun getFcmToken(): String {
        return "" // To be implemented with iOS FCM or native Push Notifications
    }
}
