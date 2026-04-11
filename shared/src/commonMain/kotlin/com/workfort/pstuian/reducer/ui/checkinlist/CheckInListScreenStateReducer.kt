package com.workfort.pstuian.reducer.ui.checkinlist

import com.workfort.pstuian.reducer.service.StateReducer


class CheckInListScreenStateReducer : StateReducer<CheckInListScreenState, CheckInListScreenStateUpdate> {
    override val initial: CheckInListScreenState
        get() = CheckInListScreenState()
}