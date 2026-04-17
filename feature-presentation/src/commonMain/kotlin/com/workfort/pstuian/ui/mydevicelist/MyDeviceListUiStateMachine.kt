package com.workfort.pstuian.ui.mydevicelist

import com.workfort.pstuian.featuredomain.model.DeviceEntity
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyDeviceListUiStateMachine : UiStateMachine<MyDeviceListUiState> {
    private val _uiState = MutableStateFlow(MyDeviceListUiState())
    override val uiState: StateFlow<MyDeviceListUiState> = _uiState.asStateFlow()

    fun updateLoading(isLoading: Boolean, isRefresh: Boolean) {
        _uiState.update {
            it.copy(
                devices = if (isRefresh) emptyList() else it.devices,
                isLoading = isLoading,
                error = if (isLoading && isRefresh) null else it.error
            )
        }
    }

    fun updateData(devices: List<DeviceEntity>, isEndOfData: Boolean) {
        _uiState.update {
            it.copy(
                devices = devices,
                isLoading = false,
                isEndOfData = isEndOfData,
            )
        }
    }

    fun updateError(message: String, isFirstPage: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = false,
                isEndOfData = true,
                error = if (isFirstPage) message else null
            )
        }
    }
}
