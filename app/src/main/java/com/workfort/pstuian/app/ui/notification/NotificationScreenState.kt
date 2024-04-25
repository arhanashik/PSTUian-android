package com.workfort.pstuian.app.ui.notification

import com.workfort.pstuian.model.NotificationEntity


data class NotificationScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val notificationState: NotificationState,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                notificationState = NotificationState.None,
                messageState = null,
            )
        }
        sealed interface NotificationState {
            data object None : NotificationState
            data class Available(
                val notifications: List<NotificationEntity>,
                val isLoading: Boolean,
            ) : NotificationState
            data class Error(val message: String) : NotificationState
        }
        sealed interface MessageState {
            data class NotificationDetails(val notification: NotificationEntity) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
    }
}