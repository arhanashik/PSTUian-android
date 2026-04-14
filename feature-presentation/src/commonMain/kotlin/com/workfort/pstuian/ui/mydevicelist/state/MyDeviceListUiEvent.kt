package com.workfort.pstuian.ui.mydevicelist.state

import com.workfort.pstuian.featuredomain.model.DeviceEntity

sealed interface MyDeviceListUiEvent {
    data object OnClickBack : MyDeviceListUiEvent
    data class OnClickItem(val item: DeviceEntity) : MyDeviceListUiEvent
    data object OnClickSignOutFromAllDevice : MyDeviceListUiEvent
    data object OnConfirmSignOutFromAll : MyDeviceListUiEvent
    data class OnLoadList(val refresh: Boolean) : MyDeviceListUiEvent
    data object MessageConsumed : MyDeviceListUiEvent
    data object NavigationConsumed : MyDeviceListUiEvent
}
