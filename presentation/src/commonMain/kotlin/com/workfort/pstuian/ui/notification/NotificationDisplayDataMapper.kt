package com.workfort.pstuian.ui.notification

import com.workfort.pstuian.featuredomain.model.Notification
import com.workfort.pstuian.ui.notification.displaydata.NotificationDisplayData
import com.workfort.pstuian.util.DateTimeUtil

internal class NotificationDisplayDataMapper(val dateTimeUtil: DateTimeUtil) {

    fun map(notifications: List<Notification>): List<NotificationDisplayData> {
        return notifications.map { notification ->
            NotificationDisplayData(
                notification = notification,
                formattedReadAt = dateTimeUtil.formatTimeHHSS(notification.readAt),
                formattedTime = dateTimeUtil.formatTimeHHSS(notification.createdAt),
                formattedDate = dateTimeUtil.formatDateDDMMMYYYY(notification.createdAt),
            )
        }
    }
}