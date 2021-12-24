package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.notification.NotificationEntity
import com.workfort.pstuian.util.remote.NotificationApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 29 Oct, 2021 at 20:55.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/10/29.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
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