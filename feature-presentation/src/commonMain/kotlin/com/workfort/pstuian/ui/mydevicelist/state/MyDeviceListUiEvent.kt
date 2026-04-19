package com.workfort.pstuian.ui.mydevicelist.state

import com.workfort.pstuian.featuredomain.model.Device

sealed interface MyDeviceListUiEvent {
    data object RefreshClicked : MyDeviceListUiEvent
    data object LoadMore : MyDeviceListUiEvent
    data object BackClicked : MyDeviceListUiEvent
    data class ItemClicked(val item: Device) : MyDeviceListUiEvent
    data object SignOutFromAllDeviceClicked : MyDeviceListUiEvent
}
