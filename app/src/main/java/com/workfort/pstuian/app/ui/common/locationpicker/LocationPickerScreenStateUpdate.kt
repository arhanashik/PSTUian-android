package com.workfort.pstuian.app.ui.common.locationpicker

import com.workfort.pstuian.model.CheckInLocationEntity
import com.workfort.pstuian.view.service.StateUpdate

sealed interface LocationPickerScreenStateUpdate : StateUpdate<LocationPickerScreenState> {

    data class ShowLocationList(
        val locations: List<CheckInLocationEntity>,
        val isLoading: Boolean,
    ) : LocationPickerScreenStateUpdate {
        override fun invoke(
            oldState: LocationPickerScreenState
        ): LocationPickerScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    locationListState = LocationPickerScreenState.DisplayState.LocationListState
                        .Available(locations = locations, isLoading = isLoading),
                ),
            )
        }
    }

    data class DataLoadFailed(val message: String) : LocationPickerScreenStateUpdate {
        override fun invoke(
            oldState: LocationPickerScreenState
        ): LocationPickerScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    locationListState = LocationPickerScreenState.DisplayState.LocationListState
                        .Error(message),
                ),
            )
        }
    }

    data class UpdateMessageState(
        val newState: LocationPickerScreenState.DisplayState.MessageState
    ) : LocationPickerScreenStateUpdate {
        override fun invoke(oldState: LocationPickerScreenState): LocationPickerScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = newState))
        }
    }

    data object MessageConsumed : LocationPickerScreenStateUpdate {
        override fun invoke(
            oldState: LocationPickerScreenState
        ): LocationPickerScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: LocationPickerScreenState.NavigationState,
    ) : LocationPickerScreenStateUpdate {
        override fun invoke(
            oldState: LocationPickerScreenState,
        ): LocationPickerScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : LocationPickerScreenStateUpdate {
        override fun invoke(
            oldState: LocationPickerScreenState,
        ): LocationPickerScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}