package com.workfort.pstuian.ui.mydevicelist

import com.workfort.pstuian.featuredomain.model.Device
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyDeviceListUiStateMachine : UiStateMachine<MyDeviceListUiState> {
    private val _uiState = MutableStateFlow(MyDeviceListUiState())
    override val uiState: StateFlow<MyDeviceListUiState> = _uiState.asStateFlow()

    fun showLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun updateDevices(
        devices: List<Device>,
        isRefresh: Boolean,
    ) {
        val newDevices = if (isRefresh) devices else _uiState.value.devices + devices
        _uiState.update {
            it.copy(
                devices = newDevices,
                isLoading = false,
                isEndOfData = devices.isEmpty(),
            )
        }
    }

    fun updateError(message: String, isFirstPage: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = false,
                isEndOfData = true,
                error = if (isFirstPage) message else null,
            )
        }
    }
}
