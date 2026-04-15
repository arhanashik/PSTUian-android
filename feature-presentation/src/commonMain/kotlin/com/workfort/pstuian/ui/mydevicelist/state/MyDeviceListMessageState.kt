package com.workfort.pstuian.ui.mydevicelist.state

import com.workfort.pstuian.featuredomain.model.DeviceEntity

sealed interface MyDeviceListMessageState {
    data class Loading(val cancelable: Boolean) : MyDeviceListMessageState
    data class ShowDetails(val item: DeviceEntity) : MyDeviceListMessageState
    data class ConfirmSignOutFromAll(val onConfirm: () -> Unit) : MyDeviceListMessageState
    data class Success(val message: String) : MyDeviceListMessageState
    data class Error(val message: String) : MyDeviceListMessageState
}
