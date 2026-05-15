package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.Notification

interface CustomNotificationRepository {

    suspend fun getAll(
        userType: String,
        page: Int,
        forceRefresh: Boolean,
    ): DomainResult<List<Notification.CustomNotification>>

    suspend fun hasUnreadCustomNotifications(userType: String,): DomainResult<Boolean>

    suspend fun markCustomNotificationAsRead(
        notification: Notification.CustomNotification,
    ): DomainResult<Notification.CustomNotification>
}