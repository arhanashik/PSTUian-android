package com.workfort.pstuian.ui.locationpicker.state

import com.workfort.pstuian.featuredomain.model.CheckInLocationEntity

data class LocationPickerUiState(
    val locationListState: LocationListState = LocationListState.None,
    val messageState: MessageState? = null,
    val navigationState: LocationPickerNavigationState? = null,
) {
    sealed interface LocationListState {
        data object None : LocationListState
        data class Available(
            val locations: List<CheckInLocationEntity>,
            val isLoading: Boolean,
        ) : LocationListState
        data class Error(val message: String) : LocationListState
    }

    sealed interface MessageState {
        data class ConfirmAddLocation(val locationName: String) : MessageState
        data class Success(val message: String) : MessageState
        data class Error(val message: String) : MessageState
    }
}
