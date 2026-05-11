package com.workfort.pstuian.ui.notification.state

sealed interface NotificationNavigationState {
    data object GoBack : NotificationNavigationState
}
