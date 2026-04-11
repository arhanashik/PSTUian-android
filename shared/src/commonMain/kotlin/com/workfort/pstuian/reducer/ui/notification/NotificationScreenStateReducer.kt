package com.workfort.pstuian.reducer.ui.notification

import com.workfort.pstuian.reducer.service.StateReducer


class NotificationScreenStateReducer : StateReducer<NotificationScreenState, NotificationScreenStateUpdate> {
    override val initial: NotificationScreenState
        get() = NotificationScreenState()
}