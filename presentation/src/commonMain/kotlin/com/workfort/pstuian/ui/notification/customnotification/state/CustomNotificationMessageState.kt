package com.workfort.pstuian.ui.notification.customnotification.state

sealed interface CustomNotificationMessageState {
    data class ShowAlert(val title: String = "Error", val message: String) : CustomNotificationMessageState
    data class Snackbar(val message: String) : CustomNotificationMessageState
}
