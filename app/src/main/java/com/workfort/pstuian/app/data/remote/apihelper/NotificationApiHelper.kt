package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.notification.NotificationEntity

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
interface NotificationApiHelper {
    suspend fun getAll(userId: Int, userType: String): List<NotificationEntity>
}