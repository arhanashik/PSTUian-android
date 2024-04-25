package com.workfort.pstuian.app.ui.common.locationpicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.CheckInLocationEntity
import com.workfort.pstuian.repository.CheckInLocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationPickerViewModel(
    private val isCheckInMode: Boolean,
    private val checkInLocationRepo: CheckInLocationRepository,
    private val reducer: LocationPickerScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<LocationPickerScreenState> get() = _screenState

    private fun updateScreenState(update: LocationPickerScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(LocationPickerScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(LocationPickerScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        LocationPickerScreenStateUpdate.NavigateTo(
            LocationPickerScreenState.NavigationState.GoBack(
                selectedLocationId = null,
            )
        )
    )

    fun onClickAddLocation() {
        val locationName = queryCache
        val error = if (locationName.isEmpty()) {
            "*Required"
        } else if (locationName.length > 50) {
            "*Max length is 50"
        } else {
            ""
        }
        val state = if (error.isEmpty()) {
            LocationPickerScreenStateUpdate.UpdateMessageState(
                LocationPickerScreenState.DisplayState.MessageState.ConfirmAddLocation(locationName),
            )
        } else {
            LocationPickerScreenStateUpdate.UpdateMessageState(
                LocationPickerScreenState.DisplayState.MessageState.Error(error),
            )
        }
        updateScreenState(state)
    }

    fun onClickLocation(location: CheckInLocationEntity) {
        updateScreenState(
            LocationPickerScreenStateUpdate.NavigateTo(
                LocationPickerScreenState.NavigationState.GoBack(
                    selectedLocationId = location.id,
                )
            )
        )
    }

    private fun isLocationListLoading(): Boolean {
        val state = _screenState.value.displayState.locationListState
        return if (state is LocationPickerScreenState.DisplayState.LocationListState.Available) {
            state.isLoading
        } else {
            false
        }
    }

    private var checkInLocationPage = 0
    private var queryCache: String = ""
    private val locationListCache = arrayListOf<CheckInLocationEntity>()
    fun search(query: String, refresh: Boolean) {
        if (isLocationListLoading()) {
            return
        }
        if (refresh) {
            checkInLocationPage = 0
            locationListCache.clear()
        }
        queryCache = query
        checkInLocationPage += 1
        updateScreenState(
            LocationPickerScreenStateUpdate.ShowLocationList(
                locations = locationListCache,
                isLoading = true,
            ),
        )
        viewModelScope.launch {
            runCatching {
                val locations = checkInLocationRepo.search(queryCache, checkInLocationPage)
                locationListCache.addAll(locations)
                updateScreenState(
                    LocationPickerScreenStateUpdate.ShowLocationList(
                        locations = locationListCache,
                        isLoading = false,
                    ),
                )
            }.onFailure {
                val message = it.message ?: "Failed to search"
                updateScreenState(
                    LocationPickerScreenStateUpdate.DataLoadFailed(message),
                )
            }
        }
    }

    fun createNewLocation(name: String) {
        viewModelScope.launch {
            runCatching {
                checkInLocationRepo.insert(name)
                val message = "Location create request is successful. Please wait for an admin to approve it!"
                search(query = "", refresh = true)
                updateScreenState(
                    LocationPickerScreenStateUpdate.UpdateMessageState(
                        LocationPickerScreenState.DisplayState.MessageState.Success(message),
                    ),
                )
            }.onFailure {
                val message = it.message ?: "Failed to create new location"
                updateScreenState(
                    LocationPickerScreenStateUpdate.UpdateMessageState(
                        LocationPickerScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }
}