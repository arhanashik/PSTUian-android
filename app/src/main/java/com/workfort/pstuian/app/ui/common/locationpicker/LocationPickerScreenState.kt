package com.workfort.pstuian.app.ui.common.locationpicker

import com.workfort.pstuian.model.CheckInLocationEntity


data class LocationPickerScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val locationListState: LocationListState,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                locationListState = LocationListState.None,
                messageState = null,
            )
        }

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

    sealed interface NavigationState {
        data class GoBack(val selectedLocationId: Int?) : NavigationState
    }
}