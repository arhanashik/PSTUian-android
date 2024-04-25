package com.workfort.pstuian.app.ui.checkinlist

import com.workfort.pstuian.view.service.StateReducer


class CheckInListScreenStateReducer : StateReducer<CheckInListScreenState, CheckInListScreenStateUpdate> {
 override val initial: CheckInListScreenState
  get() = CheckInListScreenState()
}