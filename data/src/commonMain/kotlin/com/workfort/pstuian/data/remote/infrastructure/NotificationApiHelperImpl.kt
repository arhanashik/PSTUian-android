package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.model.CustomNotificationDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.NotificationApiHelper
import com.workfort.pstuian.data.remote.safeApiCall
import com.workfort.pstuian.data.remote.service.CustomNotificationApiService

class NotificationApiHelperImpl(private val service: CustomNotificationApiService) : NotificationApiHelper {

    override suspend fun getAll(
        userType: String,
        page: Int,
        limit: Int,
    ): NetworkResult<List<CustomNotificationDto>> {
        return safeApiCall { service.getAll(userType, page, limit) }
    }

    override suspend fun hasUnreadCustomNotifications(userType: String): NetworkResult<Boolean> {
        return safeApiCall { service.hasUnreadCustomNotifications(userType) }
    }

    override suspend fun markCustomNotificationAsRead(notificationId: String): NetworkResult<Unit> {
        return safeApiCall { service.markCustomNotificationAsRead(notificationId) }
    }
}
