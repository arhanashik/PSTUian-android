package com.workfort.pstuian.networking.domain

import com.workfort.pstuian.model.NotificationEntity

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
interface NotificationApiHelper {
    suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int
    ): List<NotificationEntity>
}