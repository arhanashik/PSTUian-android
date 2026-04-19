package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.model.NotificationDto
import com.workfort.pstuian.data.remote.domain.NotificationApiHelper
import com.workfort.pstuian.data.remote.service.NotificationApiService

class NotificationApiHelperImpl(
    private val service: NotificationApiService
) : NotificationApiHelper {

    override suspend fun getAll(
        userId: String,
        userType: String,
        page: Int,
        limit: Int,
    ): List<NotificationDto> {
        val response = service.getAll(userId, userType, page, limit)
        return response.data?: emptyList()
    }
}