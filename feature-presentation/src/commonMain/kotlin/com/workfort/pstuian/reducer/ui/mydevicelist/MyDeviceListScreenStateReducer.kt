package com.workfort.pstuian.reducer.ui.mydevicelist

import com.workfort.pstuian.reducer.service.StateReducer


class MyDeviceListScreenStateReducer : StateReducer<MyDeviceListScreenState, MyDeviceListScreenStateUpdate> {
    override val initial: MyDeviceListScreenState
        get() = MyDeviceListScreenState()
}
