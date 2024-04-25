package com.workfort.pstuian.app.ui.mydevicelist

import com.workfort.pstuian.view.service.StateReducer


class MyDeviceListScreenStateReducer : StateReducer<MyDeviceListScreenState, MyDeviceListScreenStateUpdate> {
 override val initial: MyDeviceListScreenState
  get() = MyDeviceListScreenState()
}