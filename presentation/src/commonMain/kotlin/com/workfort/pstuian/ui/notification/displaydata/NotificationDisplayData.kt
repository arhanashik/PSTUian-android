package com.workfort.pstuian.ui.notification.displaydata

import com.workfort.pstuian.featuredomain.model.Notification

data class NotificationDisplayData(
    val notification: Notification,
    val formattedReadAt: String,
    val formattedTime: String,
    val formattedDate: String,
)
