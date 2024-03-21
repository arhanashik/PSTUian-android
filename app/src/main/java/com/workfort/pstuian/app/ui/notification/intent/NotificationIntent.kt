package com.workfort.pstuian.app.ui.notification.intent

sealed class NotificationIntent {
    data class GetAll(val page: Int) : NotificationIntent()
}