package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.Notification

interface CustomNotificationRepository {
    suspend fun getCustomNotifications(userId: Int): DomainResult<List<Notification.CustomNotification>>

    suspend fun hasUnreadCustomNotifications(): Boolean

    suspend fun markCustomNotificationAsRead(userId: Int, notificationId: String): DomainResult<Unit>
}