package com.workfort.pstuian.ui.notification.notification.state

sealed interface NotificationNavigationState {
    data object GoBack : NotificationNavigationState
}
