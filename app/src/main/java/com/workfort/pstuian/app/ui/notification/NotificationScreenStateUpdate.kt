package com.workfort.pstuian.app.ui.notification

import com.workfort.pstuian.model.NotificationEntity
import com.workfort.pstuian.view.service.StateUpdate


sealed interface NotificationScreenStateUpdate : StateUpdate<NotificationScreenState> {

    data class NotificationData(
        val notifications: List<NotificationEntity>,
        val isLoading: Boolean,
    ) : NotificationScreenStateUpdate {
        override fun invoke(
            oldState: NotificationScreenState
        ): NotificationScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    notificationState = NotificationScreenState.DisplayState.NotificationState
                        .Available(notifications, isLoading)
                )
            )
        }
    }

    data class NotificationLoadFailed(val message: String) : NotificationScreenStateUpdate {
        override fun invoke(
            oldState: NotificationScreenState
        ): NotificationScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    notificationState = NotificationScreenState.DisplayState.NotificationState
                        .Error(message)
                )
            )
        }
    }

    data class UpdateMessageState(
        val messageState: NotificationScreenState.DisplayState.MessageState
    ) : NotificationScreenStateUpdate {
        override fun invoke(
            oldState: NotificationScreenState
        ): NotificationScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : NotificationScreenStateUpdate {
        override fun invoke(
            oldState: NotificationScreenState
        ): NotificationScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: NotificationScreenState.NavigationState,
    ) : NotificationScreenStateUpdate {
        override fun invoke(
            oldState: NotificationScreenState,
        ): NotificationScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : NotificationScreenStateUpdate {
        override fun invoke(
            oldState: NotificationScreenState,
        ): NotificationScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}