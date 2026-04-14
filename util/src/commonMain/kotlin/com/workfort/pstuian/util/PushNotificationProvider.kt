package com.workfort.pstuian.util

interface PushNotificationProvider {
    suspend fun getPushToken(): String?
}