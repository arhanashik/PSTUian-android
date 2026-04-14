package com.workfort.pstuian.ui.locationpicker

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.locationpicker.state.LocationPickerNavigationState
import com.workfort.pstuian.ui.locationpicker.state.LocationPickerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LocationPickerUiStateMachine : UiStateMachine<LocationPickerUiState> {
    private val _uiState = MutableStateFlow(LocationPickerUiState())
    override val uiState: StateFlow<LocationPickerUiState> = _uiState.asStateFlow()

    fun updateLocationListState(locationListState: LocationPickerUiState.LocationListState) {
        _uiState.update { it.copy(locationListState = locationListState) }
    }

    fun updateMessageState(messageState: LocationPickerUiState.MessageState?) {
        _uiState.update { it.copy(messageState = messageState) }
    }

    fun navigateTo(navigationState: LocationPickerNavigationState?) {
        _uiState.update { it.copy(navigationState = navigationState) }
    }
}
