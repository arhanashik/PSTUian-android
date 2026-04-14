package com.workfort.pstuian.ui.notification.state

import com.workfort.pstuian.featuredomain.model.NotificationEntity

sealed interface NotificationMessageState {
    data class NotificationDetails(val notification: NotificationEntity) : NotificationMessageState
}
