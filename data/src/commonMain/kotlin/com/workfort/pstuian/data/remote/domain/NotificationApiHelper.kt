package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.CustomNotificationDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.NetworkConst

interface NotificationApiHelper {

    suspend fun getAll(
        userType: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<CustomNotificationDto>>

    suspend fun hasUnreadCustomNotifications(userType: String): NetworkResult<Boolean>

    suspend fun markCustomNotificationAsRead(notificationId: String): NetworkResult<Unit>
}
