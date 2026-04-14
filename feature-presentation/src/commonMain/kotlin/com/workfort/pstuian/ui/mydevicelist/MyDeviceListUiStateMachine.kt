package com.workfort.pstuian.ui.mydevicelist

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.DeviceEntity
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyDeviceListUiStateMachine : UiStateMachine<MyDeviceListUiState> {
    private val _uiState = MutableStateFlow(MyDeviceListUiState())
    override val uiState: StateFlow<MyDeviceListUiState> = _uiState.asStateFlow()

    fun onClickBack() {
        _uiState.update {
            it.copy(
                navigationState = MyDeviceListUiState.NavigationState.GoBack(
                    isSignedOutFromAll = false
                )
            )
        }
    }

    fun onClickItem(item: DeviceEntity) {
        _uiState.update {
            it.copy(
                messageState = MyDeviceListUiState.MessageState.ShowDetails(item)
            )
        }
    }

    fun onClickSignOutFromAllDevice(hasDevices: Boolean) {
        if (!hasDevices) {
            _uiState.update {
                it.copy(
                    messageState = MyDeviceListUiState.MessageState.Error("No device to sign out")
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    messageState = MyDeviceListUiState.MessageState.ConfirmSignOutFromAll
                )
            }
        }
    }

    fun messageConsumed() {
        _uiState.update { it.copy(messageState = null) }
    }

    fun navigationConsumed() {
        _uiState.update { it.copy(navigationState = null) }
    }

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

    fun updateMessageState(messageState: MyDeviceListUiState.MessageState?) {
        _uiState.update { it.copy(messageState = messageState) }
    }

    fun onSignedOutFromAll() {
        _uiState.update {
            it.copy(
                messageState = null,
                navigationState = MyDeviceListUiState.NavigationState.GoBack(
                    isSignedOutFromAll = true
                ),
            )
        }
    }
}
