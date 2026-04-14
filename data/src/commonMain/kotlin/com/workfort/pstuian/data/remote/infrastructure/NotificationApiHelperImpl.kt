package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.dto.NotificationDto
import com.workfort.pstuian.data.remote.domain.NotificationApiHelper
import com.workfort.pstuian.data.remote.service.NotificationApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 29 Oct, 2021 at 20:55.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class NotificationApiHelperImpl(
    private val service: NotificationApiService
) : NotificationApiHelper {
    override suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int
    ): List<NotificationDto> {
        val response = service.getAll(userId, userType, page, limit)
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }
}