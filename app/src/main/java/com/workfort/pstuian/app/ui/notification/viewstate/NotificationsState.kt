package com.workfort.pstuian.app.ui.notification.viewstate

import com.workfort.pstuian.app.data.local.notification.NotificationEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 29 Oct, 2021 at 21:10.
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

sealed class NotificationsState {
    object Idle : NotificationsState()
    object Loading : NotificationsState()
    data class Notifications(val notifications: List<NotificationEntity>) : NotificationsState()
    data class Error(val error: String?): NotificationsState()
}
