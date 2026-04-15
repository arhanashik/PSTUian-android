package com.workfort.pstuian.ui.mydevicelist.state

sealed interface MyDeviceListNavigationState {
    data object GoBack : MyDeviceListNavigationState
}
