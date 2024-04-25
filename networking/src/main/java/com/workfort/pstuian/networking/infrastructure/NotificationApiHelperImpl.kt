package com.workfort.pstuian.networking.infrastructure

import com.workfort.pstuian.model.NotificationEntity
import com.workfort.pstuian.networking.domain.NotificationApiHelper
import com.workfort.pstuian.networking.service.NotificationApiService

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
    ): List<NotificationEntity> {
        val response = service.getAll(userId, userType, page, limit)
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }
}