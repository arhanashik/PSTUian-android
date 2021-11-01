package com.workfort.pstuian.app.data.repository

import com.workfort.pstuian.app.data.local.notification.NotificationEntity
import com.workfort.pstuian.app.data.remote.apihelper.NotificationApiHelper

/**
 *  ****************************************************************************
 *  * Created by : arhan on 29 Oct, 2021 at 21:01 PM.
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

class NotificationRepository(
    private val helper: NotificationApiHelper
) {
    suspend fun getAll(): List<NotificationEntity> {
        return helper.getAll()
    }
}