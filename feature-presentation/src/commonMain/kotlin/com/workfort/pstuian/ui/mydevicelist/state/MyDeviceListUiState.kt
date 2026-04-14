package com.workfort.pstuian.ui.mydevicelist.state

import androidx.compose.runtime.Immutable
import com.workfort.pstuian.featuredomain.model.DeviceEntity

@Immutable
data class MyDeviceListUiState(
    val devices: List<DeviceEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isEndOfData: Boolean = false,
    val error: String? = null,
    val messageState: MessageState? = null,
    val navigationState: NavigationState? = null,
) {
    sealed interface MessageState {
        data class Loading(val cancelable: Boolean) : MessageState
        data class ShowDetails(val item: DeviceEntity) : MessageState
        data object ConfirmSignOutFromAll : MessageState
        data class Success(val message: String) : MessageState
        data class Error(val message: String) : MessageState
    }

    sealed interface NavigationState {
        data class GoBack(val isSignedOutFromAll: Boolean) : NavigationState
    }
}
