package com.workfort.pstuian.app.ui.mydevicelist

import com.workfort.pstuian.model.DeviceEntity

sealed class MyDeviceListScreenUiEvent {
    data object None : MyDeviceListScreenUiEvent()
    data class OnLoadMoreData(val refresh: Boolean) : MyDeviceListScreenUiEvent()
    data object OnClickBack : MyDeviceListScreenUiEvent()
    data class OnClickItem(val item: DeviceEntity) : MyDeviceListScreenUiEvent()
    data class OnClickDelete(val item: DeviceEntity) : MyDeviceListScreenUiEvent()
    data object OnClickSignOutFromAll : MyDeviceListScreenUiEvent()
    data object OnSignOutFromAll : MyDeviceListScreenUiEvent()
    data object MessageConsumed : MyDeviceListScreenUiEvent()
    data object NavigationConsumed : MyDeviceListScreenUiEvent()
}