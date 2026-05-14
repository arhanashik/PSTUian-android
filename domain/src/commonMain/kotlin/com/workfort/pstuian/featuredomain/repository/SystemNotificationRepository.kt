package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.SystemNotification
import com.workfort.pstuian.featuredomain.model.SystemNotificationDisplayType
import kotlinx.coroutines.flow.Flow

interface SystemNotificationRepository {
    fun observeSystemNotifications(userId: String): Flow<List<SystemNotification>>

    fun observeUnreadSystemNotifications(): Flow<List<SystemNotification>>

    fun observeNewSystemNotification(type: SystemNotificationDisplayType): Flow<SystemNotification>

    suspend fun markSystemNotificationAsRead(userId: String, notificationId: String): DomainResult<Unit>

    fun updateSystemNotificationClosedTimestamp(type: SystemNotificationDisplayType)
}
