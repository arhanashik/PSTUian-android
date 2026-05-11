package com.workfort.pstuian.ui.locationpicker

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.featuredomain.repository.CheckInLocationRepository
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.locationpicker.state.LocationPickerNavigationState
import com.workfort.pstuian.ui.locationpicker.state.LocationPickerUiState
import kotlinx.coroutines.launch

class LocationPickerViewModel(
    private val isCheckInMode: Boolean,
    private val checkInLocationRepo: CheckInLocationRepository,
    private val sharedScreenData: SharedScreenData,
    private val stateMachine: LocationPickerUiStateMachine,
) : UiStateMachineViewModel<LocationPickerUiState>(stateMachine) {

    override fun onUiReady() {
        search(query = "", refresh = true)
    }

    fun messageConsumed() = stateMachine.updateMessageState(null)

    fun navigationConsumed() = stateMachine.navigateTo(null)

    fun onClickBack() = stateMachine.navigateTo(
        LocationPickerNavigationState.GoBack(
            selectedLocationId = null,
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
            LocationPickerUiState.MessageState.ConfirmAddLocation(locationName)
        } else {
            LocationPickerUiState.MessageState.Error(error)
        }
        stateMachine.updateMessageState(state)
    }

    fun onClickLocation(location: CheckInLocation) {
        stateMachine.navigateTo(
            LocationPickerNavigationState.GoBack(
                selectedLocationId = location.id,
            )
        )
    }

    private fun isLocationListLoading(): Boolean {
        val state = stateMachine.uiState.value.locationListState
        return if (state is LocationPickerUiState.LocationListState.Available) {
            state.isLoading
        } else {
            false
        }
    }

    private var checkInLocationPage = 0
    private var queryCache: String = ""
    private val locationListCache = arrayListOf<CheckInLocation>()
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
        stateMachine.updateLocationListState(
            LocationPickerUiState.LocationListState.Available(
                locations = ArrayList(locationListCache),
                isLoading = true,
            ),
        )
        viewModelScope.launch {
            runCatching {
                val locations = checkInLocationRepo.search(queryCache, checkInLocationPage)
                locationListCache.addAll(locations)
                stateMachine.updateLocationListState(
                    LocationPickerUiState.LocationListState.Available(
                        locations = ArrayList(locationListCache),
                        isLoading = false,
                    ),
                )
            }.onFailure {
                val message = it.message ?: "Failed to search"
                stateMachine.updateLocationListState(
                    LocationPickerUiState.LocationListState.Error(message),
                )
            }
        }
    }

    fun createNewLocation(name: String) {
        val userId = sharedScreenData.getCurrentUser()?.authUserId ?: return
        val userType = sharedScreenData.getCurrentUserType() ?: return

        viewModelScope.launch {
            runCatching {
                checkInLocationRepo.insert(userId, userType, name)
                val message = "Location create request is successful. Please wait for an admin to approve it!"
                search(query = "", refresh = true)
                stateMachine.updateMessageState(
                    LocationPickerUiState.MessageState.Success(message),
                )
            }.onFailure {
                val message = it.message ?: "Failed to create new location"
                stateMachine.updateMessageState(
                    LocationPickerUiState.MessageState.Error(message),
                )
            }
        }
    }
}