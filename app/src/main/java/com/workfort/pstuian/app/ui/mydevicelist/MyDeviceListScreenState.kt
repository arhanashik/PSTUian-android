package com.workfort.pstuian.app.ui.mydevicelist

import com.workfort.pstuian.model.DeviceEntity


data class MyDeviceListScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val listState: DeviceListState,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                listState = DeviceListState.None,
                messageState = null,
            )
        }

        sealed interface DeviceListState {
            data object None : DeviceListState
            data class Available(
                val items: List<DeviceEntity>,
                val isLoading: Boolean,
            ) : DeviceListState
            data class Error(val message: String) : DeviceListState
        }

        sealed interface MessageState {
            data class Loading(val cancelable: Boolean) : MessageState
            data class ShowDetails(val item: DeviceEntity) : MessageState
            data object ConfirmSignOutFromAll : MessageState
            data class Success(val message: String) : MessageState
            data class Error(val message: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data class GoBack(val isSignedOutFromAll: Boolean) : NavigationState
    }
}