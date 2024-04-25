package com.workfort.pstuian.app.ui.notification

import com.workfort.pstuian.view.service.StateReducer


class NotificationScreenStateReducer : StateReducer<NotificationScreenState, NotificationScreenStateUpdate> {
 override val initial: NotificationScreenState
  get() = NotificationScreenState()
}