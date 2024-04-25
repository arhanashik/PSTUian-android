package com.workfort.pstuian.app.ui.mycheckinlist

import com.workfort.pstuian.view.service.StateReducer


class MyCheckInListScreenStateReducer : StateReducer<MyCheckInListScreenState, MyCheckInListScreenStateUpdate> {
 override val initial: MyCheckInListScreenState
  get() = MyCheckInListScreenState()
}