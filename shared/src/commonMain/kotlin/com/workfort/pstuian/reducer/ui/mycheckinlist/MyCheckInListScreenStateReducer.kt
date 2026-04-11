package com.workfort.pstuian.reducer.ui.mycheckinlist

import com.workfort.pstuian.reducer.service.StateReducer


class MyCheckInListScreenStateReducer : StateReducer<MyCheckInListScreenState, MyCheckInListScreenStateUpdate> {
    override val initial: MyCheckInListScreenState
        get() = MyCheckInListScreenState()
}