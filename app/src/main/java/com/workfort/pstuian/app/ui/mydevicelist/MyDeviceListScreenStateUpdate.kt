package com.workfort.pstuian.app.ui.mydevicelist

import com.workfort.pstuian.model.DeviceEntity
import com.workfort.pstuian.view.service.StateUpdate

sealed interface MyDeviceListScreenStateUpdate : StateUpdate<MyDeviceListScreenState> {

    data class ShowDataList(
        val items: List<DeviceEntity>,
        val isLoading: Boolean,
    ) : MyDeviceListScreenStateUpdate {
        override fun invoke(
            oldState: MyDeviceListScreenState
        ): MyDeviceListScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    listState = MyDeviceListScreenState.DisplayState.DeviceListState
                        .Available(items = items, isLoading = isLoading),
                ),
            )
        }
    }

    data class DataLoadFailed(val message: String) : MyDeviceListScreenStateUpdate {
        override fun invoke(
            oldState: MyDeviceListScreenState
        ): MyDeviceListScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    listState = MyDeviceListScreenState.DisplayState.DeviceListState.Error(message),
                ),
            )
        }
    }

    data class UpdateMessageState(
        val newState: MyDeviceListScreenState.DisplayState.MessageState
    ) : MyDeviceListScreenStateUpdate {
        override fun invoke(oldState: MyDeviceListScreenState): MyDeviceListScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = newState))
        }
    }

    data object MessageConsumed : MyDeviceListScreenStateUpdate {
        override fun invoke(
            oldState: MyDeviceListScreenState
        ): MyDeviceListScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: MyDeviceListScreenState.NavigationState,
    ) : MyDeviceListScreenStateUpdate {
        override fun invoke(
            oldState: MyDeviceListScreenState,
        ): MyDeviceListScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : MyDeviceListScreenStateUpdate {
        override fun invoke(
            oldState: MyDeviceListScreenState,
        ): MyDeviceListScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}